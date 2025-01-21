package com.example.jiranbulletinboard.Domain.Post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public Page<PostDTO> findPost(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PostEntity> postEntities = postRepository.findAll(pageable);
        return postEntities.map(PostEntity::toDTO);
    }

    public PostDTO createPost(PostDTO postDTO) {
        PostEntity postEntity = postDTO.toEntity();
        PostEntity savedPost = postRepository.save(postEntity);
        return savedPost.toDTO();
    }

    public PostDTO selectPost(Integer id) {
        PostEntity postEntity = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
        return postEntity.toDTO();
    }

    public PostDTO updatePost(Integer id, PostDTO postDTO) {
        PostEntity postEntity = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
        postEntity.setTitle(postDTO.getTitle());
        postEntity.setCategory(postDTO.getCategory());
        postEntity.setContent(postDTO.getContent());
        PostEntity updatedPost = postRepository.save(postEntity);
        return updatedPost.toDTO();
    }

    public void deletePost(Integer id) {
        PostEntity postEntity = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
        postRepository.delete(postEntity);
    }
}