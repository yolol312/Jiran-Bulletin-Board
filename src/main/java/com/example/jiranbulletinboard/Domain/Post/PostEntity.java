package com.example.jiranbulletinboard.Domain.Post;

import com.example.jiranbulletinboard.Domain.Category.CategoryEntity;
import com.example.jiranbulletinboard.Domain.User.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "post")
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer postId;

    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    @Column(nullable = false, columnDefinition = "JSON")
    private String content;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private UserEntity user;

    public PostDTO toDTO() {
        return PostDTO.builder()
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