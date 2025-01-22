package com.example.jiranbulletinboard.Security.SessionTokenManager;

import com.example.jiranbulletinboard.Constant.ErrorMessage;
import com.example.jiranbulletinboard.Security.SessionToken.AccessToken;
import com.example.jiranbulletinboard.Security.SessionToken.SessionToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

public class AccessTokenManager implements SessionTokenManager {
    private final String secret = "your_secret_key1"; // 나중에 키 생성해서 넣기
    private final long jwtExpirationInMs = 3600000;

    @Override
    public AccessToken generateToken(String key, Object data) {
        if (!(data instanceof Map)) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_INPUT_TOKEN_DATA_TYPE);
        }
        Map<String, Object> dataMap = (Map<String, Object>) data;

        return new AccessToken(Jwts.builder()
                .setClaims(dataMap)
                .setSubject(key)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(SignatureAlgorithm.HS512, secret.getBytes())
                .compact());
    }

    @Override
    public boolean validateToken(SessionToken token) {
        try {
            Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token.getToken());
            return true;
        } catch (Exception e) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_INPUT_TOKEN_DATA);
        }
    }

    @Override
    public String getSubjectFromToken(SessionToken token) {
        Claims claims = extractClaims(token);
        return claims.getSubject();
    }

    @Override
    public Claims extractClaims(SessionToken token) {
        return Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token.getToken()).getBody();
    }
}
