package com.optimagrowth.authentication.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@NoArgsConstructor
public class ServiceConfig {

    @Value("${signing.key}")
    private String jwtSigningKey;

}
