package com.optimagrowth.license.service.client;

import com.optimagrowth.license.model.Organization;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component("REST")
@RequiredArgsConstructor
public class OrganizationRestTemplateClient implements ClientHandler {

    private final RestTemplate restTemplate;

    @Override
    public Organization getOrganization(String organizationId) {
        log.info("REST");
        return restTemplate.exchange(
                "http://organization-service/v1/organization/{organizationId}",
                HttpMethod.GET, null,
                Organization.class, organizationId
        ).getBody();
    }
}
