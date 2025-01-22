package com.example.jiranbulletinboard.Domain.Category;

import com.example.jiranbulletinboard.Domain.Position.PositionEntity;
import com.example.jiranbulletinboard.Domain.Position.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public List<CategoryEntity> getAllCategory() {
        return categoryService.getAllCategory();
    }
}
