package com.drofff.checkers.client.controller;

import com.drofff.checkers.client.document.Credentials;
import com.drofff.checkers.client.game.CheckersGame;
import com.drofff.checkers.client.service.AuthenticationService;
import com.drofff.checkers.client.service.MessageProcessor;
import io.rsocket.metadata.WellKnownMimeType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Controller;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.net.URI;

import static com.drofff.checkers.client.utils.ConsoleUtils.getUserInput;
import static com.drofff.checkers.client.utils.ConsoleUtils.printStrMono;
import static reactor.core.publisher.Mono.error;

@Controller
public class ConsoleController {

    private final AuthenticationService authenticationService;
    private final MessageProcessor messageProcessor;

    private final RSocketRequester.Builder requesterBuilder;

    private final String serverUrl;

    public ConsoleController(AuthenticationService authenticationService, MessageProcessor messageProcessor,
                             RSocketRequester.Builder requesterBuilder, @Value("${checkers.server.url}") String serverUrl) {
        this.authenticationService = authenticationService;
        this.messageProcessor = messageProcessor;
        this.requesterBuilder = requesterBuilder;
        this.serverUrl = serverUrl;
    }

    @PostConstruct
    private void start() {
        printBanner();
        RSocketRequester secureRequester = getSecureConnection().block();
        String opponentNickname = getUserInput("Opponent nickname: ");
        new CheckersGame(secureRequester, messageProcessor).start(opponentNickname).blockLast();
    }

    private void printBanner() {
        System.out.println("=================");
        System.out.println("The Checkers Game");
        System.out.println("=================");
    }

    private Mono<RSocketRequester> getSecureConnection() {
        return authenticationService.getSavedCredentials()
                .switchIfEmpty(error(new FileNotFoundException("No saved credentials")))
                .onErrorResume(this::isFileNotFoundException, e -> authenticateUser())
                .flatMap(this::secureRSocketRequester);
    }

    private boolean isFileNotFoundException(Throwable throwable) {
        return throwable instanceof FileNotFoundException;
    }

    private Mono<Credentials> authenticateUser() {
        String email = getUserInput("email: ");
        String password = getUserInput("password: ");
        Credentials credentials = new Credentials(email, password);
        Mono<String> messageMono = authenticationService.authenticate(credentials);
        return printStrMono(messageMono).thenReturn(credentials);
    }

    private Mono<RSocketRequester> secureRSocketRequester(Credentials credentials) {
        URI serverURI = URI.create(serverUrl);
        return requesterBuilder.setupMetadata(credentials.toUsernamePasswordMetadata(), getAuthMimeType())
                .connectWebSocket(serverURI);
    }

    private MimeType getAuthMimeType() {
        WellKnownMimeType rSocketAuthenticationType = WellKnownMimeType.MESSAGE_RSOCKET_AUTHENTICATION;
        return MimeTypeUtils.parseMimeType(rSocketAuthenticationType.getString());
    }

}