package com.example.jiranbulletinboard.Security;

import com.example.jiranbulletinboard.Security.SessionToken.AccessToken;
import com.example.jiranbulletinboard.Security.SessionToken.RefreshToken;
import com.example.jiranbulletinboard.Security.SessionToken.SessionToken;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        if ("/favicon.ico".equals(request.getRequestURI()) || "/fetch.js".equals(request.getRequestURI()) || "/user/login".equals(request.getRequestURI()) || "/user/register".equals(request.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }

        AccessToken token = new AccessToken(getTokenFromRequest(request));

        if (jwtUtil.validateToken(token)) {
            CustomAuthenticationToken authentication = createAuthentication(token, request);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            // Access Token이 만료된 경우 Refresh Token을 사용하여 새로 발급
            String refreshTokenKey = getRefreshTokenFromRequest(request);
            RefreshToken refreshToken = new RefreshToken(redisTemplate.opsForValue().get(Objects.requireNonNull(refreshTokenKey)));
            if (jwtUtil.validateToken(refreshToken)) {
                String url = "http://localhost:8081/user/refresh";
                HttpHeaders headers = new HttpHeaders();
                headers.set("Cookie", "refreshToken=" + refreshToken);  // 쿠키에 refreshToken 포함

                HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);

                RestTemplate restTemplate = new RestTemplate();
                String tokens = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class).getBody();


                // response에서 새로운 Access Token을 받기 위해서는 응답 형식을 확인해야 합니다.
                String[] tokenArray = tokens.split(":");
                AccessToken newJwtToken = new AccessToken(tokenArray[0]);
                RefreshToken newRefreshToken = new RefreshToken(tokenArray[1]);

                // 헤더와 쿠키 설정
                response.setHeader("Authorization", "Bearer " + newJwtToken);
                Cookie cookie = new Cookie("refreshToken", newRefreshToken.getToken());
                cookie.setHttpOnly(true);
                cookie.setPath("/");
                response.addCookie(cookie);

                CustomAuthenticationToken authentication = createAuthentication(newJwtToken, request);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        chain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private String getRefreshTokenFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private CustomAuthenticationToken createAuthentication(SessionToken token, HttpServletRequest request) {
        String email = jwtUtil.getSubjectFromToken(token);
        Long userId = jwtUtil.getUserIdFromToken(token);
        String userName = jwtUtil.getUserNameFromToken(token);
        String role = jwtUtil.getRoleFromToken(token);
        String title = jwtUtil.getTitleFromToken(token);
        String position = jwtUtil.getPositionFromToken(token);

        Object details = new WebAuthenticationDetailsSource().buildDetails(request);
        return new CustomAuthenticationToken(email, userId, userName, role, title, position, details, true);
    }
}