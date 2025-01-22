package com.example.jiranbulletinboard.Domain.Post;

import com.example.jiranbulletinboard.Domain.Category.CategoryEntity;
import com.example.jiranbulletinboard.Domain.File.FileEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public Page<PostDTO> findPost(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PostEntity> postEntities = postRepository.findAll(pageable);
        return postEntities.map(PostEntity::toDTO);
    }

    public void createPost(PostDTO postDTO, MultipartFile[] files) {
        PostEntity postEntity = postDTO.toEntity();
        List<FileEntity> fileEntities = new ArrayList<>();

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                try {
                    // 예: 로컬 디렉토리에 저장
                    String uploadDir = "/" + postDTO.getUserId() + "/" + postDTO.getPostId();
                    String filePath = uploadDir + file.getOriginalFilename();
                    file.transferTo(new File(filePath));

                    FileEntity fileEntity = FileEntity.builder()
                            .fileName(file.getOriginalFilename())
                            .filePath(filePath)
                            .post(postEntity)
                            .build();

                    fileEntities.add(fileEntity);
                } catch (IOException e) {
                    throw new RuntimeException("파일 저장 중 오류 발생", e);
                }
            }
        }
        postEntity.setFiles(fileEntities);
        postRepository.save(postEntity);
    }

    public PostDTO selectPost(Integer id) {
        PostEntity postEntity = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
        return postEntity.toDTO();
    }

    public PostDTO updatePost(Integer id, PostDTO postDTO) {
        PostEntity postEntity = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
        CategoryEntity categoryEntity = CategoryEntity.builder().categoryId(postDTO.getCategoryId()).build();
        List<FileEntity> fileEntity = new ArrayList<>();
        for (Integer file : postDTO.getFiles()) {
            fileEntity.add(FileEntity.builder().fileId(file).build());
        }

        postEntity.setTitle(postDTO.getTitle());
        postEntity.setCategory(categoryEntity);
        postEntity.setContent(postDTO.getContent());
        postEntity.setIsBulletin(postDTO.getIsBulletin());
        postEntity.setFiles(fileEntity);
        PostEntity updatedPost = postRepository.save(postEntity);
        return updatedPost.toDTO();
    }

    public void deletePost(Integer id) {
        PostEntity postEntity = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
        postRepository.delete(postEntity);
    }
}