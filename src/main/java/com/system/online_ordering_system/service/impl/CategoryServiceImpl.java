package com.system.online_ordering_system.service.impl;

import com.system.online_ordering_system.dto.CategoryDto;
import com.system.online_ordering_system.entity.Category;
import com.system.online_ordering_system.repo.CategoryRepo;
import com.system.online_ordering_system.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepo  categoryRepo;

    @Override
    public void addCategory(CategoryDto categoryDto) {
        Category category = new Category();
        category.setCategoryName(categoryDto.getCategoryName());
        categoryRepo.save(category);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepo.findAll();
    }

    @Override
    public Category getCategoryById(int categoryId) {
        return categoryRepo.findById(categoryId).get();
    }
}
