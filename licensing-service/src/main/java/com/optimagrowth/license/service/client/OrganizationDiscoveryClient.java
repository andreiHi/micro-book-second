package com.optimagrowth.license.service.client;

import com.optimagrowth.license.model.Organization;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Slf4j
@Component("DISCOVERY")
@RequiredArgsConstructor
public class OrganizationDiscoveryClient implements ClientHandler {

    private final DiscoveryClient discoveryClient;

    @Override
    public Organization getOrganization(String organizationId) {
        log.info("DISCOVERY");
        RestTemplate restTemplate = new RestTemplate();
        List<ServiceInstance> instances = discoveryClient.getInstances("organization-service");

        if (instances.isEmpty()) return null;
        String serviceUri = UriComponentsBuilder.fromUriString(instances.get(0).getUri().toString())
                .pathSegment("v1").pathSegment("organization").pathSegment(organizationId).build().toUriString();

        ResponseEntity<Organization> restExchange =
                restTemplate.exchange(
                        serviceUri,
                        HttpMethod.GET,
                        null, Organization.class);

        return restExchange.getBody();
    }
}
