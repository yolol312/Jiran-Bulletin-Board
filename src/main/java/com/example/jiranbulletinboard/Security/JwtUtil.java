package com.example.jiranbulletinboard.Security;

import com.example.jiranbulletinboard.Security.SessionToken.AccessToken;
import com.example.jiranbulletinboard.Security.SessionToken.RefreshToken;
import com.example.jiranbulletinboard.Security.SessionToken.SessionToken;
import com.example.jiranbulletinboard.Security.SessionTokenManager.AccessTokenManager;
import com.example.jiranbulletinboard.Security.SessionTokenManager.RefreshTokenManager;
import com.example.jiranbulletinboard.Security.SessionTokenManager.SessionTokenManager;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private final AccessTokenManager accessTokenManager;
    private final RefreshTokenManager refreshTokenManager;

    public JwtUtil() {
        try {
            this.accessTokenManager = new AccessTokenManager();
            this.refreshTokenManager = new RefreshTokenManager();
        } catch (Exception e) {
            throw new RuntimeException("Error initializing TokenManager", e);
        }
    }

    public AccessToken generateAccessToken(final String email, final Long userId, final String userName, final String role, final String title, final String position, final Object details) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("userName", userName);
        claims.put("role", role);
        claims.put("title", title);
        claims.put("position", position);
        claims.put("details", details);

        return accessTokenManager.generateToken(email, claims);
    }

    public RefreshToken generateRefreshToken(final String refreshTokenKey) {
        return refreshTokenManager.generateToken(refreshTokenKey, new HashMap<>());
    }

    public boolean validateToken(SessionToken token) {
        if(token instanceof AccessToken) {
            return accessTokenManager.validateToken(token);
        }
        return refreshTokenManager.validateToken(token);
    }

    public String getSubjectFromToken(SessionToken token) {
        Claims claims = extractClaims(token);
        return claims.getSubject();
    }

    public String getRoleFromToken(SessionToken token) {
        Claims claims = extractClaims(token);
        return (String) claims.get("role");  // 클레임에서 역할(role) 값 추출
    }

    public String getUserNameFromToken(SessionToken token) {
        Claims claims = extractClaims(token);
        return (String) claims.get("username");  // 클레임에서 사용자 이름(username) 값 추출
    }

    public String getTitleFromToken(SessionToken token) {
        Claims claims = extractClaims(token);
        return (String) claims.get("title");  // 클레임에서 직급(title) 값 추출
    }

    public String getPositionFromToken(SessionToken token) {
        Claims claims = extractClaims(token);
        return (String) claims.get("position");  // 클레임에서 직책(position) 값 추출
    }

    public Long getUserIdFromToken(SessionToken token) {
        Claims claims = extractClaims(token);
        Object userId = claims.get("userId");
        if (userId instanceof Integer) {
            return ((Integer) userId).longValue();
        } else if (userId instanceof String) {
            return Long.parseLong((String) userId);
        } else {
            throw new IllegalArgumentException("Invalid userId type");
        }
    }

    private Claims extractClaims(SessionToken token) {
        if(token instanceof AccessToken) {
            return accessTokenManager.extractClaims(token);
        }
        return refreshTokenManager.extractClaims(token);
    }
}