package com.example.jiranbulletinboard.Domain.File;

import com.example.jiranbulletinboard.Domain.Post.PostEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FileService {
    @Autowired
    private FileRepository fileRepository;

    public List<FileDTO> selectFileByPostId(PostEntity postEntity) {
        List<FileEntity> fileEntity = fileRepository.findByPost(postEntity);
        return fileEntity.stream().map(FileEntity::toDTO).toList();
    }

    public FileDTO getFileById(Integer fileId) {
        Optional<FileEntity> fileEntity = fileRepository.findById(fileId);
        return fileEntity.map(FileEntity::toDTO).orElse(null);
    }
}
