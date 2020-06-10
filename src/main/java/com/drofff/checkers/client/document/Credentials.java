package com.drofff.checkers.client.document;

import org.springframework.security.rsocket.metadata.UsernamePasswordMetadata;

public class Credentials {

    private String email;

    private String password;

    public Credentials() {}

    public Credentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UsernamePasswordMetadata toUsernamePasswordMetadata() {
        return new UsernamePasswordMetadata(email, password);
    }

}