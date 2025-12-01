package com.e_commerce.e_commerce.service.impl;

import com.e_commerce.e_commerce.model.Cart;
import com.e_commerce.e_commerce.model.Product;
import com.e_commerce.e_commerce.model.UserDtls;
import com.e_commerce.e_commerce.repository.CartRepository;
import com.e_commerce.e_commerce.repository.ProductRepository;
import com.e_commerce.e_commerce.repository.UserRepository;
import com.e_commerce.e_commerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Cart saveCart(Integer productId , Integer userId)
    {
        UserDtls userDtls = userRepository.findById(userId).get();
        Product product = productRepository.findById(productId).get();

        Cart cartStatus = cartRepository.findByProductIdAndUserId(productId,userId);
        Cart cart = null;

        if(ObjectUtils.isEmpty(cartStatus))
        {
            cart = new Cart();
            cart.setUser(userDtls);
            cart.setProduct(product);
            cart.setQuantity(1);
            cart.setTotalPrice(1 * cart.getProduct().getDiscountPrice());
        }else
        {
            cart = cartStatus;
            cart.setQuantity(cart.getQuantity()+1);
            cart.setTotalPrice(cart.getQuantity() * cart.getProduct().getDiscountPrice());

        }
        Cart saveCart =  cartRepository.save(cart);
        return saveCart;
    }

    public List<Cart> getCartsByUser(Integer userId)
    {
        return  null;
    }
}
