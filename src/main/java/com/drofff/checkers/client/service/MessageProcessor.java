package com.drofff.checkers.client.service;

import com.drofff.checkers.client.message.TextMessage;
import reactor.core.publisher.Mono;

public interface MessageProcessor {

    Mono<String> processTextMessage(TextMessage textMessage);

}