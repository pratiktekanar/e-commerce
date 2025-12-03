package com.e_commerce.e_commerce.service.impl;

import com.e_commerce.e_commerce.model.*;
import com.e_commerce.e_commerce.repository.CartRepository;
import com.e_commerce.e_commerce.repository.ProductOrderRepository;
import com.e_commerce.e_commerce.service.OrderService;
import com.e_commerce.e_commerce.util.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductOrderRepository productOrderRepository;

    @Override
    public void saveOrder(Integer userid, OrderRequest orderRequest)
    {
        List<Cart> carts = cartRepository.findByUserId(userid);

        for(Cart cart:carts)
        {
            ProductOrder order = new ProductOrder();
            order.setOrderId(UUID.randomUUID().toString());
            order.setOrderDate(new Date());
            order.setProduct(cart.getProduct());
            order.setPrice(cart.getProduct().getDiscountPrice());
            order.setQuantity(cart.getQuantity());
            order.setUser(cart.getUser());
            order.setStatus(OrderStatus.IN_PROGRESS.getName());
            order.setPaymentType(orderRequest.getPaymentType());

            OrderAddress address = new OrderAddress();
            address.setFirstName(orderRequest.getFirstName());
            address.setLastName(orderRequest.getLastName());
            address.setEmail(orderRequest.getEmail());
            address.setMobileNo(orderRequest.getMobileNo());
            address.setAddress(orderRequest.getAddress());
            address.setCity(orderRequest.getCity());
            address.setState(orderRequest.getState());
            address.setPincode(orderRequest.getPincode());

            order.setOrderAddress(address);

            productOrderRepository.save(order);

        }
    }

    @Override
    public List<ProductOrder> getOrdersByUser(UserDtls user)
    {
        return  productOrderRepository.getOrdersByUser(user);
    }

    @Override
    public Boolean orderUpdateStatus(Integer id,String status)
    {
        Optional<ProductOrder> order = productOrderRepository.findById(id);
        if (order.isPresent())
        {
            ProductOrder productOrder = order.get();
            productOrder.setStatus(status);
            productOrderRepository.save(productOrder);
            return true;
        }
        return false;
    }

    @Override
    public List<ProductOrder> getAllOrders()
    {
        return productOrderRepository.findAll();
    }
}
