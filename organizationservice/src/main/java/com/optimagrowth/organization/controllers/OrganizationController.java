package com.optimagrowth.organization.controllers;

import com.optimagrowth.organization.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.optimagrowth.organization.models.Organization;

import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping(value="/v1/organization")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService service;

    @GetMapping(value="/{organizationId}")
    public ResponseEntity<Organization> getOrganization(@PathVariable("organizationId") String organizationId) {
        log.info("getOrganization");
        return ResponseEntity.ok(service.findById(organizationId));
    }

    @PutMapping(value="/{organizationId}")
    public void updateOrganization( @PathVariable("organizationId") String id, @RequestBody Organization organization) {
        service.update(organization);
    }

    @PostMapping
    public ResponseEntity<Organization>  saveOrganization(@RequestBody Organization organization) {
        return ResponseEntity.ok(service.create(organization));
    }

    @DeleteMapping(value="/{organizationId}")
    public CompletableFuture<ResponseEntity<Void>> deleteOrganization(@PathVariable("organizationId") String id) {
        return  CompletableFuture.runAsync(() -> service.delete(id))
                .thenApply(aVoid -> ResponseEntity.noContent().build());
    }

}
