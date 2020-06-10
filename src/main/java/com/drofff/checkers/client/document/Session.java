package com.drofff.checkers.client.document;

public class Session {

    private String id;

    private Board gameBoard;

    private String sessionOwnerId;

    private String sessionMemberId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Board getGameBoard() {
        return gameBoard;
    }

    public void setGameBoard(Board gameBoard) {
        this.gameBoard = gameBoard;
    }

    public String getSessionOwnerId() {
        return sessionOwnerId;
    }

    public void setSessionOwnerId(String sessionOwnerId) {
        this.sessionOwnerId = sessionOwnerId;
    }

    public String getSessionMemberId() {
        return sessionMemberId;
    }

    public void setSessionMemberId(String sessionMemberId) {
        this.sessionMemberId = sessionMemberId;
    }

}