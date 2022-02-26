package com.optimagrowth.organization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

/**
 * Аннотация @EnableResourceServer сообщает Spring Cloud и Spring Security, что служба является защищенным ресурсом.
 * @EnableResourceServer применяет фильтр, который перехватывает все входящие вызовы службы,
 * проверяет наличие маркера доступа OAuth2 в HTTP-заголовке входящего вызова, а затем выполняет обратный вызов на
 * URL-адрес обратного вызова, определенный в файле security.oauth2.resource.userInfoUri, чтобы убедиться,
 * что токен действителен. Узнав, что токен действителен, аннотация @EnableResourceServer также применяет любые
 * правила контроля доступа в отношении того, кто и что может получить доступ к службе.
 */
@SpringBootApplication
@RefreshScope
@EnableResourceServer
public class OrganizationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrganizationServiceApplication.class, args);
    }

}
