package com.example.jiranbulletinboard.Domain.Post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/post")
@ResponseBody
public class PostController {
    @Autowired
    private PostService postService;

    // 게시글 불러 오기
    @GetMapping("/find/all")
    public Page<PostDetails> findPostAll(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) String keyword) {
        return postService.findPost(page, size, categoryId, keyword);
    }

    /*
    @GetMapping("/find")
    public ResponseEntity<Page<PostDetails>> getPostsWithDetails(@RequestParam Integer page, @RequestParam Integer size) {
        Page<PostDetails> postsWithDetails = postService.findPost(page, size);
        return ResponseEntity.ok(postsWithDetails);
    }
    */
    // 특정 게시글 작성
    @PostMapping("/writing")
    public void createPost(@ModelAttribute PostDTO postDTO, @RequestParam("multipartFiles") MultipartFile[] multipartFiles) {
        postService.createPost(postDTO, multipartFiles);
    }

    // 특정 게시글 조회(검색 필드를 파라미터로 받음)
    @GetMapping("/writing/{id}")
    public PostDTO selectPost(@PathVariable Integer id) {
        return postService.selectPost(id);
    }

    // 특정 게시글 수정(Only 작성자)
    @PutMapping("/writing/{id}")
    public void updatePost(@PathVariable Integer id, @ModelAttribute PostDTO postDTO) {
        postService.updatePost(id, postDTO);
    }

    // 특정 게시글 삭제(Only 작성자)
    @DeleteMapping("/writing/{id}")
    public void deletePost(@PathVariable Integer id) {
        postService.deletePost(id);
    }
}

