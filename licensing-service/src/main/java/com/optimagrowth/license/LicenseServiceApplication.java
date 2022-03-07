package com.optimagrowth.license;

import com.optimagrowth.license.event.MyBinder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@Slf4j
@SpringBootApplication
@RefreshScope
@EnableDiscoveryClient
@EnableFeignClients
//Tells Spring Cloud we are going to use Hystrix for our service
@EnableCircuitBreaker
@EnableResourceServer
@EnableBinding(value = {MyBinder.class})
public class LicenseServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LicenseServiceApplication.class, args);
    }

}
