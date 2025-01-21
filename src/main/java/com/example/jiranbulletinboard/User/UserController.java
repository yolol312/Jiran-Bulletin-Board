package com.example.jiranbulletinboard.User;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "user/loginPage";
    }

    @GetMapping("/register")
    public String register() {
        return "user/registerPage";
    }

    //사용자 정보 불러 오기(토큰에 있는 ID or Email로)
    @GetMapping("/info")
    public String info() {
        return "user/registerPage";
    }

    @GetMapping("/initLogin")
    public ResponseEntity<Map<String, String>> initLogin() {
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("publicKey", userService.getPublicKey());
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody UserDTO userDTO, HttpServletRequest request, HttpServletResponse response) {
        String tokens = userService.authenticateUser(userDTO.getEmail(), userDTO.getPassword(), request);
        if (tokens != null) {
            String[] tokenArray = tokens.split(":");
            String jwtToken = tokenArray[0];
            String refreshToken = tokenArray[1];

            Cookie cookie = new Cookie("refreshToken", refreshToken);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);

            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("accessToken", jwtToken);

            return ResponseEntity.ok(responseBody);
        } else {
            // 로그인 실패 시 적절한 응답 반환
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid email or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    @PostMapping("/refresh")
    public String refresh(HttpServletRequest request) {
        return userService.refreshAccessToken(request);
    }

    @PostMapping("/register")
    public String register(UserDTO userDTO) {
        userService.registerUser(userDTO);
        return "redirect:/user/login";
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    // Invalidate the refresh token in Redis
                    userService.deleteRefreshToken(cookie.getValue());

                    // Remove the cookie
                    cookie.setValue(null);
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                    break;
                }
            }
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
