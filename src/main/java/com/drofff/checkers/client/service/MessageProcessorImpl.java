package com.drofff.checkers.client.service;

import com.drofff.checkers.client.message.TextMessage;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.drofff.checkers.client.utils.ValidationUtils.validateNotErrorMessage;

@Service
public class MessageProcessorImpl implements MessageProcessor {

    @Override
    public Mono<String> processTextMessage(TextMessage textMessage) {
        return validateNotErrorMessage(textMessage)
                .map(TextMessage::getPayload);
    }

}