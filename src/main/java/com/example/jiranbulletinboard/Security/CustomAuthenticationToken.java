package com.example.jiranbulletinboard.Security;

import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Getter
public class CustomAuthenticationToken implements Authentication {

    private final String email;
    private final Long userId;
    private final String userName;
    private final String role;
    private final String title;
    private final String position;
    private final Object details;

    private boolean authenticated;

    // 생성자
    public CustomAuthenticationToken(final String email, final Long userId, final String userName, final String role, final String title, final String position, final Object details, final boolean authenticated) {
        this.email = email;
        this.userId = userId;
        this.userName = userName;
        this.role = role;
        this.title = title;
        this.position = position;
        this.details = details;
        this.authenticated = authenticated;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return details;
    }

    @Override
    public Object getPrincipal() {
        return email;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return userName + " (" + email + ")";
    }
}

