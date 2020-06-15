package com.drofff.checkers.client.message;

import com.drofff.checkers.client.document.Board;
import com.drofff.checkers.client.document.Step;
import com.drofff.checkers.client.enums.BoardSide;
import com.drofff.checkers.client.enums.MessageType;
import com.drofff.checkers.client.utils.JsonUtils;

import java.util.HashMap;
import java.util.Map;

public class SessionMessage implements Message {

    private MessageType messageType;

    private Map<String, String> payload = new HashMap<>();

    @Override
    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    @Override
    public Map<String, String> getPayload() {
        return payload;
    }

    public void setPayload(Map<String, String> payload) {
        this.payload = payload;
    }

    public Board getGameBoard() {
        String boardJson = payload.get("board");
        return JsonUtils.parseValueOfClassFromJson(Board.class, boardJson);
    }

    public BoardSide getUserSide() {
        String userSide = payload.get("userSide");
        return BoardSide.valueOf(userSide);
    }

    public String getUserId() {
        return payload.get("userId");
    }

    public Step getStep() {
        String stepJson = payload.get("step");
        return JsonUtils.parseValueOfClassFromJson(Step.class, stepJson);
    }

    public Boolean movedKing() {
        String movedKingStr = payload.get("isKing");
        return Boolean.valueOf(movedKingStr);
    }

    public String getMessageText() {
        return payload.get("messageText");
    }

}