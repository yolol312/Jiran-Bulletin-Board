package com.example.jiranbulletinboard.Domain.Category;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CategoryDTO {
    private Integer categoryId;
    private String categoryName;

    public CategoryEntity toEntity() {
        return CategoryEntity.builder()
                .categoryId(this.categoryId)
                .categoryName(this.categoryName)
                .build();
    }
}
