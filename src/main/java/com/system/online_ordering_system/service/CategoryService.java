package com.system.online_ordering_system.service;

import com.system.online_ordering_system.dto.CategoryDto;
import com.system.online_ordering_system.entity.Category;

import java.util.List;

public interface CategoryService {
    void addCategory(CategoryDto categoryDto);

    List<Category> getAllCategories();

    Category getCategoryById(int categoryId);

}
