package com.optimagrowth.organization.events.source;

import com.optimagrowth.organization.events.model.MyBinder;
import com.optimagrowth.organization.events.model.OrganizationChangeModel;
import com.optimagrowth.organization.utils.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SimpleSourceBean {

    private final MyBinder myBinder;

    public void publishOrganizationChange(String action, String organizationId) {
        log.debug("Sending Kafka message {} for Organization Id: {}", action, organizationId);
        OrganizationChangeModel changeModel = OrganizationChangeModel.builder()
                .type(OrganizationChangeModel.class.getTypeName())
                .action(action)
                .organizationId(organizationId)
                .correlationId(UserContext.getCorrelationId())
                .build();
        myBinder.orderOut().send(MessageBuilder.withPayload(changeModel).build());
    }

}
