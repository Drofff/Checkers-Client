package com.drofff.checkers.client.service;

import com.drofff.checkers.client.document.Credentials;
import com.drofff.checkers.client.exception.CheckersClientException;
import com.drofff.checkers.client.message.TextMessage;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.drofff.checkers.client.utils.JsonUtils.parseValueOfClassFromJson;
import static com.drofff.checkers.client.utils.JsonUtils.serializeIntoJson;
import static com.drofff.checkers.client.utils.ValidationUtils.validateNotNull;
import static java.nio.file.Files.exists;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final Path CREDENTIALS_FILE_PATH = Paths.get("credentials.json");

    private final RSocketRequester rSocketRequester;

    private final MessageProcessor messageProcessor;

    public AuthenticationServiceImpl(RSocketRequester rSocketRequester, MessageProcessor messageProcessor) {
        this.rSocketRequester = rSocketRequester;
        this.messageProcessor = messageProcessor;
    }

    @Override
    public Mono<Credentials> getSavedCredentials() {
        return readCredentialsFileJson().map(credentialsJson ->
                parseValueOfClassFromJson(Credentials.class, credentialsJson));
    }

    private Mono<String> readCredentialsFileJson() {
        return exists(CREDENTIALS_FILE_PATH) ? Mono.just(readCredentialsFileContent()) :
                Mono.empty();
    }

    private String readCredentialsFileContent() {
        try {
            byte[] credentialsBytes = Files.readAllBytes(CREDENTIALS_FILE_PATH);
            return new String(credentialsBytes);
        } catch(IOException e) {
            throw new CheckersClientException(e.getMessage());
        }
    }

    @Override
    public Mono<String> authenticate(Credentials credentials) {
        validateNotNull(credentials.getEmail(), "Email is required");
        validateNotNull(credentials.getPassword(), "Password should be provided");
        return rSocketRequester.route("login")
                .data(Mono.just(credentials))
                .retrieveMono(TextMessage.class)
                .flatMap(messageProcessor::processTextMessage)
                .doOnNext(msg -> saveCredentials(credentials));
    }

    private void saveCredentials(Credentials credentials) {
        try {
            storeUserCredentials(credentials);
        } catch(IOException e) {
            throw new CheckersClientException(e.getMessage());
        }
    }

    private void storeUserCredentials(Credentials credentials) throws IOException {
        String credentialsJson = serializeIntoJson(credentials);
        Files.write(CREDENTIALS_FILE_PATH, credentialsJson.getBytes());
    }

}