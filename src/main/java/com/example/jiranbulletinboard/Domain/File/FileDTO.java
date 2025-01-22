package com.example.jiranbulletinboard.Domain.File;

import com.example.jiranbulletinboard.Domain.Post.PostEntity;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FileDTO {
    private Integer fileId;
    private String fileName;
    private String filePath;
    private Integer postId;

    public FileEntity toEntity() {
        PostEntity postEntity = PostEntity.builder().postId(this.postId).build();
        return FileEntity.builder()
                .fileId(this.fileId)
                .fileName(this.fileName)
                .filePath(this.filePath)
                .post(postEntity)
                .build();
    }
}
