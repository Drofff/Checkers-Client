package com.drofff.checkers.client.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.rsocket.messaging.RSocketStrategiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.security.rsocket.metadata.SimpleAuthenticationEncoder;

import java.net.URI;

@Configuration
public class BeansConfiguration {

    @Bean
    public RSocketRequester rSocketRequester(@Value("${checkers.server.url}") String serverUrl,
                                             RSocketRequester.Builder builder) {
        URI serverURI = URI.create(serverUrl);
        return builder.connectWebSocket(serverURI).block();
    }

    @Bean
    public RSocketStrategiesCustomizer rSocketStrategiesCustomizer() {
        return strategies -> strategies.encoder(new SimpleAuthenticationEncoder());
    }

}