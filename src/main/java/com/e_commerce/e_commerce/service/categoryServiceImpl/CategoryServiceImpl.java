package com.e_commerce.e_commerce.service.categoryServiceImpl;

import com.e_commerce.e_commerce.model.Category;
import com.e_commerce.e_commerce.repository.CategoryRepository;
import com.e_commerce.e_commerce.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;

@AllArgsConstructor
@Service

public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;
    @Override
    public Category saveCategory(Category category){
        return categoryRepository.save(category);
    }

    @Override
    public Boolean existsCategory(String name){
        return categoryRepository.existsByName(name);
    }

    @Override
    public List<Category> getAllCategory(){
        return categoryRepository.findAll();
    }

    @Override
    public Boolean deleteCategory(int id){
        Category category= categoryRepository.findById(id).orElse(null);

        if(!ObjectUtils.isEmpty(category)){
            categoryRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Category getCategoryById(int id){
        Category category= categoryRepository.findById(id).orElse(null);
        return category;
    }

    @Override
    public List<Category> getAllActiveCategory(){
        List<Category> categories= categoryRepository.findByIsActiveTrue();
        return categories;
    }

}
