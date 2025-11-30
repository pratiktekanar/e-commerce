package com.e_commerce.e_commerce.service;

import com.e_commerce.e_commerce.model.Category;

import java.util.*;

public interface CategoryService {

    public Category saveCategory(Category category);

    public Boolean existsCategory(String name);

    public List<Category> getAllCategory();

    public Boolean deleteCategory(int id);

    public Category getCategoryById(int id);

    public List<Category> getAllActiveCategory();
}
