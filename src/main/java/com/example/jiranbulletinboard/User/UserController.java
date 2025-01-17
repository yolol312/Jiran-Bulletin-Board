package com.example.jiranbulletinboard.User;

import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @GetMapping("/signup")
    public String signup() {
        return "user/signupPage";
    }

    @PostMapping("/signup")
    public String signup(UserDTO userDTO) {
        userService.registerUser(userDTO);
        return "redirect:/user/login";
    }
}
