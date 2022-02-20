package com.optimagrowth.license.controller;

import com.optimagrowth.license.model.ClientType;
import com.optimagrowth.license.model.License;
import com.optimagrowth.license.service.LicenseService;
import com.optimagrowth.license.utils.UserContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value="/v1/organization/{organizationId}/license")
public class LicenseController {

    private final LicenseService licenseService;


    @GetMapping(value="/{licenseId}/{clientType}")
    public ResponseEntity<License> getLicense(@PathVariable("organizationId") String organizationId,
                                              @PathVariable("licenseId") String licenseId, @PathVariable("clientType") ClientType clientType) {
        License license = licenseService.getLicense(licenseId, organizationId, clientType);
        license.add(
                linkTo(methodOn(LicenseController.class).getLicense(organizationId,
                        license.getLicenseId(), clientType)).withSelfRel(),
                linkTo(methodOn(LicenseController.class).createLicense(license, null)).withRel("createLicense"),
                linkTo(methodOn(LicenseController.class).updateLicense(license)).withRel("updateLicense"),
                linkTo(methodOn(LicenseController.class).deleteLicense(organizationId, null,
                        license.getLicenseId())).withRel("deleteLicense"));
        return ResponseEntity.ok(license);
    }

    @PutMapping
    public ResponseEntity<License> updateLicense(@RequestBody License request) {
        return ResponseEntity.ok(licenseService.updateLicense(request));
    }

    @PostMapping
    public ResponseEntity<License> createLicense(@RequestBody License request,
                                                @RequestHeader(value = "Accept-Language", required = false) Locale locale) {
        return ResponseEntity.ok(licenseService.createLicense(request, locale));
    }

    @DeleteMapping(value="/{licenseId}")
    public ResponseEntity<String> deleteLicense(@PathVariable("organizationId") String organizationId,
                                                @RequestHeader(value = "Accept-Language", required = false) Locale locale,
                                                @PathVariable("licenseId") String licenseId) {
        return ResponseEntity.ok(licenseService.deleteLicense(licenseId, locale));
    }

    @GetMapping()
    public List<License> getLicenses(@PathVariable("organizationId") String organizationId) {
        log.info("LicenseServiceController Correlation id: {}",
                UserContextHolder.getContext().getCorrelationId());
        return licenseService.getLicensesByOrganization(organizationId);
    }
}
