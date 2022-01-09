package com.optimagrowth.license.service;

import com.optimagrowth.license.config.ServiceConfig;
import com.optimagrowth.license.model.License;
import com.optimagrowth.license.repository.LicenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class LicenseService {
    private final MessageSource messageSource;
    private final LicenseRepository licenseRepository;
    private final ThreadLocalRandom random =  ThreadLocalRandom.current();
    private final ServiceConfig config;

    public License getLicense(String licenseId, String organizationId) {
        final License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format(messageSource.getMessage("license.search.error.message", null, null),
                                licenseId, organizationId)));
        return license.withComment(config.getProperty());
    }

    public License createLicense(License license, Locale locale){
        license.setLicenseId(UUID.randomUUID().toString());
        licenseRepository.save(license);
        return license.withComment(config.getProperty());
    }

    public License updateLicense(License license){
        licenseRepository.save(license);
        return license.withComment(config.getProperty());
    }

    public String deleteLicense(String licenseId, Locale locale){
        String responseMessage = null;
        License license = new License();
        license.setLicenseId(licenseId);
        licenseRepository.delete(license);
        responseMessage = String.format(messageSource.getMessage("license.delete.message", null, locale), licenseId);
        return responseMessage;
    }

}

