package com.drofff.checkers.client.service;

import com.drofff.checkers.client.document.Credentials;
import reactor.core.publisher.Mono;

public interface AuthenticationService {

    Mono<Credentials> getSavedCredentials();

    Mono<String> authenticate(Credentials credentials);

}
