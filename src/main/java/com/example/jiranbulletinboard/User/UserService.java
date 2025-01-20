package com.example.jiranbulletinboard.User;

import com.example.jiranbulletinboard.Security.JwtUtil;
import com.example.jiranbulletinboard.Security.SessionToken.AccessToken;
import com.example.jiranbulletinboard.Security.SessionToken.RefreshToken;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void registerUser(UserDTO user) {
        // 비밀번호 암호화
        String encryptedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);

        // 사용자 저장
        userRepository.save(user.toEntity());
    }
    public String authenticateUser(String email, String password, HttpServletRequest request) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity != null && bCryptPasswordEncoder.matches(password, userEntity.getPassword())) {
            AccessToken accessToken = jwtUtil.generateAccessToken(email, userEntity.getId(), userEntity.getName(), userEntity.getRole(), userEntity.getTitle(), userEntity.getPosition(), request.getRemoteAddr());
            RefreshToken refreshToken = jwtUtil.generateRefreshToken(email);
            String refreshTokenKey = UUID.randomUUID().toString();
            redisTemplate.opsForValue().set(refreshTokenKey, refreshToken.getToken(), 7, TimeUnit.DAYS);
            return accessToken.getToken() + ":" + refreshTokenKey;
        }
        return null;
    }

    public String refreshAccessToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String refreshTokenKey = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshTokenKey = cookie.getValue();
                    break;
                }
            }
        }
        RefreshToken refreshToken = new RefreshToken(redisTemplate.opsForValue().get(Objects.requireNonNull(refreshTokenKey)));
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }
        String email = jwtUtil.getSubjectFromToken(refreshToken);
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity != null) {
            AccessToken newAccessToken = jwtUtil.generateAccessToken(email, userEntity.getId(), userEntity.getName(), userEntity.getRole(), userEntity.getTitle(), userEntity.getPosition(), request.getRemoteAddr());
            RefreshToken newRefreshToken = jwtUtil.generateRefreshToken(email);
            String newRefreshTokenKey = UUID.randomUUID().toString();
            redisTemplate.opsForValue().set(newRefreshTokenKey, newRefreshToken.getToken(), 7, TimeUnit.DAYS);
            redisTemplate.delete(refreshTokenKey);
            return newAccessToken.getToken() + ":" + newRefreshTokenKey;
        }
        return null;
    }
}
