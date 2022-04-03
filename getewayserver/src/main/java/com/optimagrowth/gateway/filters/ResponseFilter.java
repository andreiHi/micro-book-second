package com.optimagrowth.gateway.filters;

import brave.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ResponseFilter {
    private final FilterUtils filterUtils;
    private final Tracer tracer;

    @Bean
    public GlobalFilter postGlobalFilter() {
        return ((exchange, chain) -> chain.filter(exchange).then(Mono.fromRunnable(() -> {
            String traceId = tracer.currentSpan().context().traceIdString();
            log.debug("Adding the correlation id to the outbound headers. {}", traceId);
            exchange.getResponse().getHeaders().add(FilterUtils.CORRELATION_ID, traceId);
            log.debug("Completing outgoing request for {}.", exchange.getRequest().getURI());
        })));
    }
}