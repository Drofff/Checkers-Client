package com.drofff.checkers.client;

import com.drofff.checkers.client.game.graphics.GraphicsContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.rsocket.RSocketSecurityAutoConfiguration;

@SpringBootApplication(exclude = {
        RSocketSecurityAutoConfiguration.class
})
public class CheckersClientApplication {

    public static void main(String[] args) {
        GraphicsContext.initJFrame();
        SpringApplication.run(CheckersClientApplication.class, args);
    }

}
