package com.optimagrowth.license.event.handler;

import com.optimagrowth.license.event.MyBinder;
import com.optimagrowth.license.event.model.OrganizationChangeModel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrganizationChangeHandler {


    @SneakyThrows
    @StreamListener(MyBinder.ORDER_IN)
    public void consumer(@Payload OrganizationChangeModel organization) {
        log.debug("Received a message of type " + organization);

        switch(organization.getAction()){
            case "GET":
                log.debug("Received a GET event from the organization service for organization id {}", organization.getOrganizationId());
                break;
            case "SAVE":
                log.debug("Received a SAVE event from the organization service for organization id {}", organization.getOrganizationId());
                break;
            case "UPDATE":
                log.debug("Received a UPDATE event from the organization service for organization id {}", organization.getOrganizationId());
                break;
            case "DELETE":
                log.debug("Received a DELETE event from the organization service for organization id {}", organization.getOrganizationId());
                break;
            default:
                log.error("Received an UNKNOWN event from the organization service of type {}", organization.getType());
                break;
        }
    }
}
