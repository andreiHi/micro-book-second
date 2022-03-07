package com.optimagrowth.organization.events.model;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface MyBinder {
    // channels
    String ORDER_OUT = "output";

    @Output(ORDER_OUT)
    MessageChannel orderOut();
}
