package com.optimagrowth.organization.service;

import brave.Tracer;
import com.optimagrowth.organization.events.source.SimpleSourceBean;
import com.optimagrowth.organization.models.Organization;
import com.optimagrowth.organization.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganizationService {
    private final OrganizationRepository repository;
    private final SimpleSourceBean simpleSourceBean;
    private final Tracer tracer;

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

    public void delete(String id) {
        Organization organization = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found with id" + id));
        repository.delete(organization);
        simpleSourceBean.publishOrganizationChange("DELETE", organization.getId());
    }
}
