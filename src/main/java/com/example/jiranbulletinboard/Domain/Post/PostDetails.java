package com.example.jiranbulletinboard.Domain.Post;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PostDetails {
    private Integer postId;
    private String title;
    private String content;
    private Integer userId;
    private String userName;
    private Integer categoryId;
    private String categoryName;
    private LocalDateTime createdAt;
    private Boolean isBulletin;
}
