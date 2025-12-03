package com.e_commerce.e_commerce.repository;

import com.e_commerce.e_commerce.model.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductOrderRepository extends JpaRepository<ProductOrder,Integer> {
}
