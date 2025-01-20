package com.example.jiranbulletinboard.Security.SessionToken;

import lombok.Getter;

@Getter
public class RefreshToken implements SessionToken {
    private final String token;

    public RefreshToken(String token) {
        this.token = token;
    }
}
