package com.drofff.checkers.client.utils;

import com.drofff.checkers.client.exception.ValidationException;
import com.drofff.checkers.client.message.Message;
import reactor.core.publisher.Mono;

import static reactor.core.publisher.Mono.error;

public class ValidationUtils {

    private ValidationUtils() {}

    public static void validateNotNull(Object obj, String errorMessage) {
        if(obj == null) {
            throw new ValidationException(errorMessage);
        }
    }

    public static <T extends Message> Mono<T> validateNotErrorMessage(T message) {
        if(message.isError()) {
            String messageText = message.getPayload().toString();
            return error(new ValidationException(messageText));
        }
        return Mono.just(message);
    }

}