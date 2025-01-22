package com.example.jiranbulletinboard.Domain.Post;

import com.example.jiranbulletinboard.Domain.File.FileDTO;
import com.example.jiranbulletinboard.Domain.File.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostFacadeService {
    @Autowired
    private PostService postService;

    @Autowired
    private FileService fileService;

    public PostDetails selectPostDetail(Integer postId) {
        return postService.selectPostDetail(postId);
    }

    public List<FileDTO> selectFileByPostId(Integer postId) {
        PostEntity postEntity = postService.selectPostEntity(postId);
        return fileService.selectFileByPostId(postEntity);
    }

    public PostDTO selectPost(Integer postId) {
        return postService.selectPost(postId);
    }
}
