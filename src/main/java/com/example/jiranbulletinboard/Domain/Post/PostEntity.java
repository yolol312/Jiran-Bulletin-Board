package com.example.jiranbulletinboard.Domain.Post;

import com.example.jiranbulletinboard.Domain.Vo.Category.CategoryEntity;
import com.example.jiranbulletinboard.Domain.File.FileEntity;
import com.example.jiranbulletinboard.Domain.User.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "is_bulletin", nullable = false)
    private Boolean isBulletin;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<FileEntity> files = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    public PostDTO toDTO() {
        List<Integer> files = new ArrayList<>();
        for (FileEntity file : this.files) {
            files.add(file.getFileId());
        }

        return PostDTO.builder()
                .postId(this.postId)
                .title(this.title)
                .categoryId(this.category.getCategoryId())
                .content(this.content)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .isBulletin(this.isBulletin)
                .files(files)
                .userId(this.user.getUserId())
                .build();
    }
}