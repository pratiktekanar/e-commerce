package com.e_commerce.e_commerce.repository;

import com.e_commerce.e_commerce.model.OrderAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderAddressRepository extends JpaRepository<OrderAddress,Integer> {
}
