package com.zerofinance.aip.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties(AipProperties.class)
public class AipConfiguration {

    @Autowired
    private AipProperties aipProperties;

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory());
        return restTemplate;
    }
    
    private ClientHttpRequestFactory getClientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        Integer timeout = aipProperties.getTimeout();
        if (timeout != null && timeout.intValue() > 0) {
            clientHttpRequestFactory.setConnectTimeout(timeout);
        } else {
            clientHttpRequestFactory.setConnectTimeout(10000);
        }
        return clientHttpRequestFactory;
    }
}