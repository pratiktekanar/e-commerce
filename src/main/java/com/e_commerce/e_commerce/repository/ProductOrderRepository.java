package com.e_commerce.e_commerce.repository;

import com.e_commerce.e_commerce.model.ProductOrder;
import com.e_commerce.e_commerce.model.UserDtls;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductOrderRepository extends JpaRepository<ProductOrder,Integer> {
    public List<ProductOrder> getOrdersByUser(UserDtls user);
}
