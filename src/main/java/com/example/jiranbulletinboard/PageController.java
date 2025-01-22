package com.example.jiranbulletinboard;

import com.example.jiranbulletinboard.Domain.File.FileDTO;
import com.example.jiranbulletinboard.Domain.Post.PostDTO;
import com.example.jiranbulletinboard.Domain.Post.PostDetails;
import com.example.jiranbulletinboard.Domain.Post.PostFacadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.List;


@Controller
@RequestMapping("/page")
public class PageController {
    @Autowired
    private PostFacadeService postFacadeService;

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

    @GetMapping("/writePost/{postId}")
    public String writePost(@PathVariable("postId") Integer postId, Model model) {
        PostDTO postDTO = postFacadeService.selectPost(postId);
        List<FileDTO> fileDTOs = postFacadeService.selectFileByPostId(postId);
        model.addAttribute("post", postDTO);
        model.addAttribute("files", fileDTOs);
        return "postPage/modifyPost";
    }

    @GetMapping("/myPost")
    public String myPost() {
        return "postPage/myPost";
    }

    @GetMapping("/view/{postId}")
    public String viewPost(@PathVariable("postId") Integer postId, Model model) {
        PostDetails postDetails = postFacadeService.selectPostDetail(postId);
        List<FileDTO> fileDTOs = postFacadeService.selectFileByPostId(postId);
        model.addAttribute("post", postDetails);
        model.addAttribute("files", fileDTOs);
        return "postPage/detailPage";
    }
}
