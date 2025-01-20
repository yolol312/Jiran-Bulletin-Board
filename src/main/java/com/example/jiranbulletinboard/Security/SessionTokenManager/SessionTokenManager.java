package com.example.jiranbulletinboard.Security.SessionTokenManager;

import com.example.jiranbulletinboard.Security.SessionToken.SessionToken;
import io.jsonwebtoken.Claims;

public interface SessionTokenManager {
    public SessionToken generateToken(String key, Object data);

    public boolean validateToken(SessionToken token);

    public String getSubjectFromToken(SessionToken token);
    public Claims extractClaims(SessionToken token);
}
