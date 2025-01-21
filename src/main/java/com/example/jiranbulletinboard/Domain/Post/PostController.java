package com.example.jiranbulletinboard.Domain.Post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/post")
public class PostController {
    @Autowired
    private PostService postService;

    // 게시글 불러 오기
    @GetMapping("/find")
    public Page<PostDTO> findPost(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size, Model model) {
        return postService.findPost(page, size);
    }

    // 특정 게시글 작성
    @PostMapping("/writing")
    public String createPost(@ModelAttribute PostDTO postDTO) {
        postService.createPost(postDTO);
        return "redirect:/post/bulletin";
    }

    // 특정 게시글 조회(검색 필드를 파라미터로 받음)
    @GetMapping("/writing/{id}")
    public PostDTO selectPost(@PathVariable Integer id) {
        return postService.selectPost(id);
    }

    // 특정 게시글 수정(Only 작성자)
    @PutMapping("/writing/{id}")
    public String updatePost(@PathVariable Integer id, @ModelAttribute PostDTO postDTO) {
        postService.updatePost(id, postDTO);
        return "redirect:/post/bulletin";
    }

    // 특정 게시글 삭제(Only 작성자)
    @DeleteMapping("/writing/{id}")
    public String deletePost(@PathVariable Integer id) {
        postService.deletePost(id);
        return "redirect:/post/bulletin";
    }
}