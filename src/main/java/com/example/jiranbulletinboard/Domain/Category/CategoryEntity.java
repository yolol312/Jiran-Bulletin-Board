package com.example.jiranbulletinboard.Domain.Category;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Category")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryId;

    @Column(nullable = false, unique = true)
    private String categoryName;

    public CategoryDTO toDTO() {
        return CategoryDTO.builder()
                .categoryId(this.categoryId)
                .categoryName(this.categoryName)
                .build();
    }
}
