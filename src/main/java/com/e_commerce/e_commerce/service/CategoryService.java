package com.e_commerce.e_commerce.service;

import com.e_commerce.e_commerce.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.*;

public interface CategoryService {

    public Category saveCategory(Category category);

    public Boolean existsCategory(String name);

    public List<Category> getAllCategory();

    public Boolean deleteCategory(int id);

    public Category getCategoryById(int id);

    public List<Category> getAllActiveCategory();

    public Page<Category> getAllCategoryPagination(Integer pageNo,Integer pageSize);

}
