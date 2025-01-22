package com.example.jiranbulletinboard.Domain.File;

import com.example.jiranbulletinboard.Domain.Post.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<FileEntity, Integer> {
    List<FileEntity> findByPost(PostEntity postEntity);
}
