package com.optimagrowth.license.service.client;

import com.optimagrowth.license.model.Organization;

public interface ClientHandler {

    Organization getOrganization(String organizationId);
}
