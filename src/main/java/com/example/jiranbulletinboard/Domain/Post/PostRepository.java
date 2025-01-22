package com.example.jiranbulletinboard.Domain.Post;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Integer> {
    // 카테고리와 제목을 기준으로 필터링된 게시글 조회
    @Query("SELECT new com.example.jiranbulletinboard.Domain.Post.PostDetails(p.postId, p.title, p.content, p.user.userId, u.userName, p.category.categoryId, c.categoryName, p.createdAt) " +
            "FROM PostEntity p " +
            "JOIN UserEntity u ON p.user.userId = u.userId " +
            "JOIN CategoryEntity c ON p.category.categoryId = c.categoryId " +
            "WHERE (:categoryId IS NULL OR p.category.categoryId = :categoryId) " +
            "AND (:keyword IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%'))) ")
    Page<PostDetails> findPostsWithDetails(Pageable pageable, Integer categoryId, String keyword);

    // 카테고리로 필터링된 게시글 조회
    @Query("SELECT new com.example.jiranbulletinboard.Domain.Post.PostDetails(p.postId, p.title, p.content, p.user.userId, u.userName, p.category.categoryId, c.categoryName, p.createdAt) " +
            "FROM PostEntity p " +
            "JOIN UserEntity u ON p.user.userId = u.userId " +
            "JOIN CategoryEntity c ON p.category.categoryId = c.categoryId " +
            "WHERE p.category.categoryId = :categoryId")
    Page<PostDetails> findPostsByCategory(Pageable pageable, Integer categoryId);

    // 제목으로 필터링된 게시글 조회
    @Query("SELECT new com.example.jiranbulletinboard.Domain.Post.PostDetails(p.postId, p.title, p.content, p.user.userId, u.userName, p.category.categoryId, c.categoryName, p.createdAt) " +
            "FROM PostEntity p " +
            "JOIN UserEntity u ON p.user.userId = u.userId " +
            "JOIN CategoryEntity c ON p.category.categoryId = c.categoryId " +
            "WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<PostDetails> findPostsByKeyword(Pageable pageable, String keyword);
}
