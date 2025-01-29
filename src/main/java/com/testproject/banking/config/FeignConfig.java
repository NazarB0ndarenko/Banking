package com.testproject.banking.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.testproject.banking.proxy")
public class FeignConfig {
}
