package com.drofff.checkers.client.message;

import com.drofff.checkers.client.enums.MessageType;

public interface Message {

    MessageType getMessageType();

    Object getPayload();

    default boolean isError() {
        return getMessageType() == MessageType.ERROR;
    }

}