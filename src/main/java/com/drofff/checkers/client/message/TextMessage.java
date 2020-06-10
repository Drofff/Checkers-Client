package com.drofff.checkers.client.message;

import com.drofff.checkers.client.enums.MessageType;

public class TextMessage implements Message {

    private MessageType messageType;

    private String payload;

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

}