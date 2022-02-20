package com.optimagrowth.license.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.optimagrowth.license.config.ServiceConfig;
import com.optimagrowth.license.model.ClientType;
import com.optimagrowth.license.model.License;
import com.optimagrowth.license.model.Organization;
import com.optimagrowth.license.repository.LicenseRepository;
import com.optimagrowth.license.service.client.ClientHandler;
import com.optimagrowth.license.utils.UserContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
@RequiredArgsConstructor
public class LicenseService {

    private final MessageSource messageSource;
    private final LicenseRepository licenseRepository;
    private final ServiceConfig config;
    private final Map<String, ClientHandler> clientHandlerMap;

    public License getLicense(String licenseId, String organizationId, ClientType clientType) {
        final License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format(messageSource.getMessage("license.search.error.message", null, null),
                                licenseId, organizationId)));
        Organization organization = retrieveOrganizationInfo(organizationId, clientType);
        if (null != organization) {
            license.setOrganizationName(organization.getName());
            license.setContactName(organization.getContactName());
            license.setContactEmail(organization.getContactEmail());
            license.setContactPhone(organization.getContactPhone());
        }
        return license.withComment(config.getProperty());
    }

    private Organization retrieveOrganizationInfo(String organizationId, ClientType clientType) {
        String name = clientType == ClientType.FEIGN ?
                "com.optimagrowth.license.service.client.OrganizationFeignClient" : clientType.name();
        return clientHandlerMap.getOrDefault(name,
                clientHandlerMap.get("REST")).getOrganization(organizationId);
    }

    public License createLicense(License license, Locale locale) {
        license.setLicenseId(UUID.randomUUID().toString());
        licenseRepository.save(license);
        return license.withComment(config.getProperty());
    }

    public License updateLicense(License license) {
        licenseRepository.save(license);
        return license.withComment(config.getProperty());
    }

    public String deleteLicense(String licenseId, Locale locale) {
        String responseMessage = null;
        License license = new License();
        license.setLicenseId(licenseId);
        licenseRepository.delete(license);
        responseMessage = String.format(messageSource.getMessage("license.delete.message", null, locale), licenseId);
        return responseMessage;
    }

    /**
     * Атрибут threadPoolKey определяет уникальное имя пула потоков.
     * Атрибут threadPoolProperties позволяет определить и настроить поведение threadPool.
     * Атрибут coreSize позволяет определить максимальное количество потоков в пуле потоков.
     * maxQueueSize позволяет вам определить очередь, которая находится перед вашим пулом потоков и может ставить в очередь входящие запросы.
     * CircuitBreaker.requestVolumeThreshold управляет количеством последовательных вызовов.
     * CircuitBreaker.errorThresholdPercentage — это процент вызовов, которые должны завершиться ошибкой.
     * CircuitBreaker.sleepWindowInMilliseconds указывает количество времени, в течение которого Hystrix будет спать.
     * Параметр metrics.rollingStats.timeInMilliseconds управляет размером окна, которое будет использоваться Hystrix для мониторинга проблемы.
     * Параметр metrics.rollingStats.numBuckets управляет количеством сборов статистики в определенном окне.
     * Регистратор с идентификатором корреляции, который мы собираемся определить позже.
     */
    @HystrixCommand(commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000"),
            @HystrixProperty( name="circuitBreaker.requestVolumeThreshold", value="10"),
            @HystrixProperty( name="circuitBreaker.errorThresholdPercentage", value="75"),
            @HystrixProperty( name="circuitBreaker.sleepWindowInMilliseconds", value="7000"),
            @HystrixProperty( name="metrics.rollingStats.timeInMilliseconds", value="15000"),
            @HystrixProperty( name="metrics.rollingStats.numBuckets", value="5")
    }, fallbackMethod = "buildFallbackLicenseList",
            threadPoolKey = "licenseByOrganizationThreadPool",
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize",value="30"),
                    @HystrixProperty(name="maxQueueSize", value="10")
            })
    public List<License> getLicensesByOrganization(String organizationId) {
        log.info("getLicensesByOrganization Correlation id: {}",
                UserContextHolder.getContext().getCorrelationId());
        randomlyRunLong();
        return licenseRepository.findByOrganizationId(organizationId);
    }

    /**
     * Метод для симуляции отказа работы БД
     */
    private void randomlyRunLong() {
        int randomNum = ThreadLocalRandom.current().nextInt((3 - 1) + 1) + 1;
        if (randomNum == 3) {
            sleep();
        }
    }

    private void sleep(){
        try {
            log.info("sleep");
            Thread.sleep(11000);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Метод вызывается в случае прерывания описывется в аннотации HystrixCommand fallbackMethod
     */
    private List<License> buildFallbackLicenseList(String organizationId){
        List<License> fallbackList = new ArrayList<>();
        License license = new License();
        license.setLicenseId("0000000-00-00000");
        license.setOrganizationId(organizationId);
        license.setProductName("Sorry no licensing information currently available");
        fallbackList.add(license);
        return fallbackList;
    }

}

