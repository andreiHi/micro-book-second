package com.optimagrowth.license.service.client;

import com.optimagrowth.license.model.Organization;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component("REST")
@RequiredArgsConstructor
public class OrganizationRestTemplateClient implements ClientHandler {

    private final RestTemplate restTemplate;
    private final OAuth2RestTemplate oAuth2RestTemplate;

  //  @Override
//    public Organization getOrganization(String organizationId) {
//        log.info("REST");
//        return restTemplate.exchange(
//                "http://organization-service/v1/organization/{organizationId}",
//                HttpMethod.GET, null,
//                Organization.class, organizationId
//        ).getBody();
//    }
    @Autowired
    private DiscoveryClient discoveryClient;

    public Organization getOrganization(String organizationId) {
        return oAuth2RestTemplate.exchange(
                "http://gateway-server:8072/organization/v1/organization/{organizationId}",
                HttpMethod.GET,
                null, Organization.class, organizationId).getBody();
    }
}
