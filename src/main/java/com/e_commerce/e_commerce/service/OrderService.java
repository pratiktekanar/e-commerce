package com.e_commerce.e_commerce.service;

import com.e_commerce.e_commerce.model.OrderRequest;
import com.e_commerce.e_commerce.model.ProductOrder;
import com.e_commerce.e_commerce.model.UserDtls;

import java.util.List;

public interface OrderService {

    public void saveOrder(Integer userid , OrderRequest orderRequest);

    public List<ProductOrder> getOrdersByUser(UserDtls user);

    public Boolean orderUpdateStatus(Integer id,String status);
}
