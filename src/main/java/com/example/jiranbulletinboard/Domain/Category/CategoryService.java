package com.example.jiranbulletinboard.Domain.Category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<CategoryEntity> getAllCategory() {
        return categoryRepository.findAll();
    }
}
