package com.example.jiranbulletinboard.Page;

import com.example.jiranbulletinboard.Post.PostDTO;
import com.example.jiranbulletinboard.Post.PostService;
import com.example.jiranbulletinboard.User.UserDTO;
import com.example.jiranbulletinboard.User.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/page")
public class PageController {
    private final PostService postService;

    public PageController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/login")
    public String login() {
        return "user/loginPage";
    }

    @GetMapping("/register")
    public String register() {
        return "user/registerPage";
    }

    // 게시판 페이지 불러 오기
    @GetMapping("/bulletinBoard")
    public String bulletin(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, Model model) {
        Page<PostDTO> posts = postService.findPost(page, size);
        model.addAttribute("posts", posts.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", posts.getTotalPages());
        return "post/bulletinBoard";
    }

    // 게시글 작성 페이지 불러 오기
    @GetMapping("/page/write")
    public String writePost() {
        return "post/writePost";
    }
}
