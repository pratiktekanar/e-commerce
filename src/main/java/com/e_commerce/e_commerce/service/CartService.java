package com.e_commerce.e_commerce.service;

import com.e_commerce.e_commerce.model.Cart;

import java.util.List;

public interface CartService {

    public Cart saveCart(Integer userId , Integer productId);

    public List<Cart> getCartsByUser(Integer userId);

    public Integer getCountCard(Integer userId);

    public void updateQuantity(String sy,Integer cid);
}
