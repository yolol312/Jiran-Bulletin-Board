package com.example.jiranbulletinboard.Domain.Vo.Category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {
    @Query("SELECT c.categoryName FROM CategoryEntity c WHERE c.categoryId = :categoryId")
    String findCategoryNameById(@Param("categoryId") Integer categoryId);
}
