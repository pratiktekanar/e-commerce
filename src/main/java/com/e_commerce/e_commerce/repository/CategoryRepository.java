package com.e_commerce.e_commerce.repository;
import com.e_commerce.e_commerce.model.Category;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Integer>{
    public Boolean existsByName(String name);

    public List<Category> findByIsActiveTrue();
}
