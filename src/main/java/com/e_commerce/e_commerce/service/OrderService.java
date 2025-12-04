package com.e_commerce.e_commerce.service;

import com.e_commerce.e_commerce.model.OrderRequest;
import com.e_commerce.e_commerce.model.ProductOrder;
import com.e_commerce.e_commerce.model.UserDtls;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderService {

    public void saveOrder(Integer userid , OrderRequest orderRequest);

    public List<ProductOrder> getOrdersByUser(UserDtls user);

    public ProductOrder orderUpdateStatus(Integer id,String status);

    public List<ProductOrder> getAllOrders();

    public ProductOrder getOrdersByOrderId(String orderId);

    public Page<ProductOrder> getAllOrdersPagination(Integer pageNo, Integer pageSize);
}
