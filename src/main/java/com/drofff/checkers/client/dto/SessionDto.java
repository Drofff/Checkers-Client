package com.drofff.checkers.client.dto;

public class SessionDto {

    private String nickname;

    public SessionDto(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

}