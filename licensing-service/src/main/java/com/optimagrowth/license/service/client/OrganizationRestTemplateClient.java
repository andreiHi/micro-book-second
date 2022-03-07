package com.optimagrowth.license.service.client;

import com.optimagrowth.license.model.Organization;
import com.optimagrowth.license.repository.OrganizationRedisRepository;
import com.optimagrowth.license.utils.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Slf4j
@Component("REST")
@RequiredArgsConstructor
public class OrganizationRestTemplateClient implements ClientHandler {

    private final RestTemplate restTemplate;
    //private final OAuth2RestTemplate oAuth2RestTemplate;
    private final OrganizationRedisRepository redisRepository;

    @Override
    public Organization getOrganization(String organizationId) {
        log.info("REST");
        log.debug("In Licensing Service.getOrganization: {}",
                UserContext.getCorrelationId());
        Organization organization = checkRedisCache(organizationId);
        if (Objects.nonNull(organization)) {
            log.debug("I have successfully retrieved an organization {} from the redis cache: {}",
                    organizationId, organization);
            return organization;
        }
        log.debug("Unable to locate organization from the redis cache: {}.", organizationId);
        ResponseEntity<Organization> restExchange = restTemplate.exchange(
                "http://gateway-server:8072/organization/v1/organization/{organizationId}",
                HttpMethod.GET, null,
                Organization.class, organizationId);
        organization = restExchange.getBody();
        if (Objects.nonNull(organization)) {
            cacheOrganizationObject(organization);
        }
        return organization;
    }

    @Autowired
    private DiscoveryClient discoveryClient;

//    public Organization getOrganization(String organizationId) {
//        return oAuth2RestTemplate.exchange(
//                "http://gateway-server:8072/organization/v1/organization/{organizationId}",
//                HttpMethod.GET,
//                null, Organization.class, organizationId).getBody();
//    }

    private void cacheOrganizationObject(Organization organization) {
        try {
            redisRepository.save(organization);
        } catch (Exception e) {
            log.error("Unable to cache organization {} in Redis. Exception {}",
                    organization.getId(), e);
        }
    }

    private Organization checkRedisCache(String organizationId) {
        try {
            return redisRepository.findById(organizationId)
                    .orElse(null);
        } catch (Exception ex) {
            log.error("Error encountered while trying to retrieve organization{} check Redis Cache. Exception {}",
                    organizationId, ex);
            return null;
        }
    }
}
