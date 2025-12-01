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

import java.util.ArrayList;
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

    @Override
    public List<Cart> getCartsByUser(Integer userId)
    {
        List<Cart> carts = cartRepository.findByUserId(userId);
        Double totalOrderPricce = 0.0;

        List<Cart> updateCarts = new ArrayList<>();
        for (Cart c:carts){
            Double totalPricce = c.getProduct().getDiscountPrice() * c.getQuantity();
            c.setTotalPrice(totalPricce);
            totalOrderPricce += totalPricce;
            c.setTotalOrderPrice(totalOrderPricce);
            updateCarts.add(c);
        }

        return updateCarts;
    }

    @Override
    public void updateQuantity(String sy,Integer cid)
    {
        int updateQuantity;
        Cart cart = cartRepository.findById(cid).get();
        if(sy.equalsIgnoreCase("de"))
        {
            updateQuantity = cart.getQuantity()-1;
            if(updateQuantity <= 0)
            {
                cartRepository.delete(cart);

            }else{
                cart.setQuantity(updateQuantity);
                cartRepository.save(cart);
            }
        }else
        {
            updateQuantity = cart.getQuantity()+1;
        }
        cart.setQuantity(updateQuantity);
        cartRepository.save(cart);

    }

    @Override
    public Integer getCountCard(Integer userId)
    {
        return cartRepository.countByUserId(userId);
    }

}
