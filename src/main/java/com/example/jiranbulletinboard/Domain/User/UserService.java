package com.example.jiranbulletinboard.Domain.User;

import com.example.jiranbulletinboard.Domain.Position.PositionEntity;
import com.example.jiranbulletinboard.Domain.Position.PositionRepository;
import com.example.jiranbulletinboard.Domain.Role.RoleEntity;
import com.example.jiranbulletinboard.Domain.Role.RoleRepository;
import com.example.jiranbulletinboard.Domain.Title.TitleEntity;
import com.example.jiranbulletinboard.Domain.Title.TitleRepository;
import com.example.jiranbulletinboard.Security.JwtUtil;
import com.example.jiranbulletinboard.Security.SessionToken.AccessToken;
import com.example.jiranbulletinboard.Security.SessionToken.RefreshToken;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TitleRepository titleRepository;

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private final PublicKey publicKey;
    private final PrivateKey privateKey;

    public UserService() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            KeyPair keyPair = keyGen.generateKeyPair();
            this.publicKey = keyPair.getPublic();
            this.privateKey = keyPair.getPrivate();
        } catch (Exception e) {
            throw new RuntimeException("Error initializing RSA keys", e);
        }
    }

    public String getPublicKey() {
        try {
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey.getEncoded());
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey pubKey = keyFactory.generatePublic(x509EncodedKeySpec);
            return Base64.getEncoder().encodeToString(pubKey.getEncoded());
        } catch (Exception e) {
            throw new RuntimeException("Error getting public key", e);
        }
    }

    public void registerUser(UserDTO user) {
        TitleEntity title = titleRepository.findById(user.getTitle().getTitleId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Title ID"));
        PositionEntity position = positionRepository.findById(user.getPosition().getPositionId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Position ID"));
        RoleEntity role = roleRepository.findById(user.getRole().getRoleId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Role ID"));

        // 변환된 Entity를 DTO에 설정
        user.setTitle(title);
        user.setPosition(position);
        user.setRole(role);

        // 비밀번호 암호화
        String encryptedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);

        // 사용자 저장
        userRepository.save(user.toEntity());
    }
    public String authenticateUser(String encryptedEmail, String encryptedPassword, HttpServletRequest request) {
        try {
            String email = decrypt(encryptedEmail);
            String password = decrypt(encryptedPassword);

            UserEntity userEntity = userRepository.findByEmail(email);
            if (userEntity != null && bCryptPasswordEncoder.matches(password, userEntity.getPassword())) {
                AccessToken accessToken = jwtUtil.generateAccessToken(email, userEntity.getUserId(), userEntity.getName(), userEntity.getRole().getRoleId(), userEntity.getTitle().getTitleId(), userEntity.getPosition().getPositionId(), request.getRemoteAddr());
                RefreshToken refreshToken = jwtUtil.generateRefreshToken(email);
                String refreshTokenKey = UUID.randomUUID().toString();
                redisTemplate.opsForValue().set(refreshTokenKey, refreshToken.getToken(), 7, TimeUnit.DAYS);
                return accessToken.getToken() + ":" + refreshTokenKey;
            }
        } catch (Exception e) {
            throw new RuntimeException("Error during authentication", e);
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
            AccessToken newAccessToken = jwtUtil.generateAccessToken(email, userEntity.getUserId(), userEntity.getName(), userEntity.getRole().getRoleId(), userEntity.getTitle().getTitleId(), userEntity.getPosition().getPositionId(), request.getRemoteAddr());
            RefreshToken newRefreshToken = jwtUtil.generateRefreshToken(email);
            String newRefreshTokenKey = UUID.randomUUID().toString();
            redisTemplate.opsForValue().set(newRefreshTokenKey, newRefreshToken.getToken(), 7, TimeUnit.DAYS);
            deleteRefreshToken(refreshTokenKey);
            return newAccessToken.getToken() + ":" + newRefreshTokenKey;
        }
        return null;
    }

    public void deleteRefreshToken(String refreshTokenKey) {
        redisTemplate.delete(refreshTokenKey);
    }

    private String decrypt(String encryptedData) throws Exception {
        // Decrypt the data using the stored private key
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decryptedBytes);
    }
}
