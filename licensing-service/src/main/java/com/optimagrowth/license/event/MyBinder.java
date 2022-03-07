package com.optimagrowth.license.event;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface MyBinder {
    // channels
    String ORDER_IN = "inboundOrgChanges";

    @Input(ORDER_IN)
    SubscribableChannel orderIn();

}
