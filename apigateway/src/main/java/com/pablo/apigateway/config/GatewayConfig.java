package com.pablo.apigateway.config;

import org.springframework.cloud.gateway.server.mvc.handler.RestClientProxyExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class GatewayConfig {

    @Bean
    public RestClientProxyExchange restClientProxyExchange() {
        RestClient restClient = RestClient.builder()
                .requestFactory(new org.springframework.http.client.SimpleClientHttpRequestFactory())
                .build();
        return new RestClientProxyExchange(restClient);
    }
}