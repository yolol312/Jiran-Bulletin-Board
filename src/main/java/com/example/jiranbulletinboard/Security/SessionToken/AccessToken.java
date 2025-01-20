package com.example.jiranbulletinboard.Security.SessionToken;

import lombok.Getter;

@Getter
public class AccessToken implements SessionToken {
    private final String token;

    public AccessToken(String token) {
        this.token = token;
    }
}
