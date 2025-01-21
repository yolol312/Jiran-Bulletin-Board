package com.example.jiranbulletinboard.Domain.Post;

import com.example.jiranbulletinboard.Domain.Category.CategoryEntity;
import com.example.jiranbulletinboard.Domain.User.UserEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PostDTO {
    private Integer postId;
    private String title;
    private CategoryEntity category;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserEntity user;

    public PostEntity toEntity() {
        return PostEntity.builder()
                .postId(this.postId)
                .title(this.title)
                .category(this.category)
                .content(this.content)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .user(this.user)
                .build();
    }
}