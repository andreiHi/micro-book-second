package com.optimagrowth.organization.service;

import com.optimagrowth.organization.events.source.SimpleSourceBean;
import com.optimagrowth.organization.models.Organization;
import com.optimagrowth.organization.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganizationService {
    private final OrganizationRepository repository;
    private final SimpleSourceBean simpleSourceBean;

    public Organization findById(String organizationId) {
        simpleSourceBean.publishOrganizationChange("GET", organizationId);
        return repository.findById(organizationId).orElse(null);
    }

    public Organization create(Organization organization) {
        organization.setId(UUID.randomUUID().toString());
        organization = repository.save(organization);
        simpleSourceBean.publishOrganizationChange("SAVE", organization.getId());
        return organization;

    }

    public void update(Organization organization) {
        repository.save(organization);
        simpleSourceBean.publishOrganizationChange("UPDATE", organization.getId());
    }

    public void delete(Organization organization) {
        repository.deleteById(organization.getId());
        simpleSourceBean.publishOrganizationChange("DELETE", organization.getId());
    }
}
