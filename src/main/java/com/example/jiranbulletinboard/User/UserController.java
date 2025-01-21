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

    //사용자 정보 불러 오기(토큰에 있는 ID or Email로)
    @GetMapping("/info")
    public String info() {
        return "user/registerPage";
    }

    @PostMapping("/register")
    public String register(UserDTO userDTO) {
        userService.registerUser(userDTO);
        return "redirect:/user/login";
    }
}
