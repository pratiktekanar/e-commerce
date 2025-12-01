package com.e_commerce.e_commerce.repository;

import com.e_commerce.e_commerce.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart,Integer> {

    public Cart findByProductIdAndUserId(Integer productId,Integer userId);
}
