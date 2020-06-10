package com.drofff.checkers.client.configuration;

import com.drofff.checkers.client.configuration.properties.CheckersServerProperties;
import org.springframework.boot.rsocket.messaging.RSocketStrategiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.security.rsocket.metadata.SimpleAuthenticationEncoder;

@Configuration
public class BeansConfiguration {

    @Bean
    public RSocketRequester rSocketRequester(CheckersServerProperties checkersServerProperties,
                                             RSocketRequester.Builder builder) {
        return builder.connectTcp(checkersServerProperties.getAddress(), checkersServerProperties.getPort()).block();
    }

    @Bean
    public RSocketStrategiesCustomizer rSocketStrategiesCustomizer() {
        return strategies -> strategies.encoder(new SimpleAuthenticationEncoder());
    }

}
