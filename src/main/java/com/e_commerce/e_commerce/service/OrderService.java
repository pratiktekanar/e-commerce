package com.e_commerce.e_commerce.service;

import com.e_commerce.e_commerce.model.OrderRequest;

public interface OrderService {

    public void saveOrder(Integer userid , OrderRequest orderRequest);
}
