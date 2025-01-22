package com.example.jiranbulletinboard.Domain.Post;

import com.example.jiranbulletinboard.Domain.Category.CategoryEntity;
import com.example.jiranbulletinboard.Domain.File.FileEntity;
import com.example.jiranbulletinboard.Domain.User.UserEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PostDTO {
    private Integer postId;
    private String title;
    private Integer categoryId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isBulletin;
    private List<Integer> files;
    private Integer userId;

    public PostEntity toEntity() {
        CategoryEntity categoryEntity = CategoryEntity.builder().categoryId(categoryId).build();
        UserEntity userEntity = UserEntity.builder().userId(userId).build();
        List<FileEntity> fileEntities = new ArrayList<>();
        for (Integer files : this.files) {
            fileEntities.add(FileEntity.builder().fileId(files).build());
        }

        return PostEntity.builder()
                .postId(this.postId)
                .title(this.title)
                .category(categoryEntity)
                .content(this.content)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .isBulletin(this.isBulletin)
                .files(fileEntities)
                .user(userEntity)
                .build();
    }
}