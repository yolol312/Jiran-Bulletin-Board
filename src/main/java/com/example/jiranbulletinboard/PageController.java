package com.example.jiranbulletinboard;

import com.example.jiranbulletinboard.Domain.Post.PostDTO;
import com.example.jiranbulletinboard.Domain.Post.PostDetails;
import com.example.jiranbulletinboard.Domain.Post.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/page")
public class PageController {
    @Autowired
    private final PostService postService;

    //@Autowired
    //private PagedResourcesAssembler<PostDetails> pagedResourcesAssembler;

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
    public String bulletin() {
        return "postPage/bulletinBoard";
    }

    // 게시글 작성 페이지 불러 오기
    @GetMapping("/writePost")
    public String writePost() {
        return "postPage/writePost";
    }


    @GetMapping("/view/{postId}")
    public String viewPost(@RequestParam Integer postId, Model model) {
        PostDetails postDetails = postService.selectPostDetail(postId);
        model.addAttribute("post", postDetails);
        return "postPage/detailPage";
    }
    /* 제공 해준 코드
    @GetMapping("/view")
    public ResponseEntity<PagedModel<PostDetails>> getPostsWithDetails(@RequestParam Integer page, @RequestParam Integer size) {
        Page<PostDetails> postsWithDetails = postService.findPost(page, size);
        PagedModel<PostDetails> pagedModel = pagedResourcesAssembler.toModel(postsWithDetails, WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PageController.class).getPostsWithDetails(page, size)).withSelfRel());
        return ResponseEntity.ok(pagedModel);
    }
     */
}
