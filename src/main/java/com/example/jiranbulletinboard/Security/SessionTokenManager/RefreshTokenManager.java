package com.example.jiranbulletinboard.Security.SessionTokenManager;

import com.example.jiranbulletinboard.Security.SessionToken.AccessToken;
import com.example.jiranbulletinboard.Security.SessionToken.RefreshToken;
import com.example.jiranbulletinboard.Security.SessionToken.SessionToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

public class RefreshTokenManager implements SessionTokenManager {

    private final String secret = "your_secret_key2"; // Use a strong secret key
    private final long jwtExpirationInMs = 604800000; // 1 hour

    @Override
    public RefreshToken generateToken(String key, Object data) {
        if (!(data instanceof Map)) {
            throw new IllegalArgumentException("Data must be of type Map<String, Object>");
        }
        Map<String, Object> dataMap = (Map<String, Object>) data;

        return new RefreshToken(Jwts.builder()
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
            return false;
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

    public AccessToken generateAccessTokenFromRefreshToken(Claims claims) {
        return new AccessToken(Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(SignatureAlgorithm.HS512, secret.getBytes())
                .compact());
    }
}
