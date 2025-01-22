package com.example.jiranbulletinboard.Domain.File;

import com.example.jiranbulletinboard.Domain.Post.PostEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "File")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer fileId;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String filePath;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private PostEntity post;

    public FileDTO toDTO() {
        return FileDTO.builder()
                .fileId(this.fileId)
                .fileName(this.fileName)
                .filePath(this.filePath)
                .postId(this.post.getPostId())
                .build();
    }
}
