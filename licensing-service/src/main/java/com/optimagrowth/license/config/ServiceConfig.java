package com.optimagrowth.license.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "example")
public class ServiceConfig {

    @Value("${example.property}")
    private String property;

    @Value("${signing.key}")
    private String jwtSigningKey;

}
