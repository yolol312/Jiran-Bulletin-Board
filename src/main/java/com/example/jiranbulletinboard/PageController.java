package com.example.jiranbulletinboard;

import com.example.jiranbulletinboard.Domain.Post.PostDTO;
import com.example.jiranbulletinboard.Domain.Post.PostService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/page")
public class PageController {
    private final PostService postService;

    public PageController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/login")
    public String login() {
        return "userPage/loginPage";
    }

    @GetMapping("/register")
    public String register() {
        return "userPage/registerPage";
    }

    // 게시판 페이지 불러 오기
    @GetMapping("/bulletinBoard")
    public String bulletin(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, Model model) {
        //Page<PostDTO> posts = postService.findPost(page, size);
        //model.addAttribute("posts", posts.getContent());
        //model.addAttribute("currentPage", page);
        //model.addAttribute("totalPages", posts.getTotalPages());
        return "postPage/bulletinBoard";
    }

    // 게시글 작성 페이지 불러 오기
    @GetMapping("/writePost")
    public String writePost() {
        return "postPage/writePost";
    }
}
