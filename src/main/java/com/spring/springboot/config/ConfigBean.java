package com.spring.springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ConfigBean {

    //rpc bean
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
