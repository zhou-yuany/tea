package com.tea.server;

import eleme.openapi.demo.OAuthClientDemo;
import eleme.openapi.sdk.api.exception.ServiceException;
import eleme.openapi.sdk.config.Config;
import eleme.openapi.sdk.oauth.OAuthClient;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.tea"})
@MapperScan("com.tea.server.mapper")
public class TeaManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(TeaManagementApplication.class, args);
    }

}
