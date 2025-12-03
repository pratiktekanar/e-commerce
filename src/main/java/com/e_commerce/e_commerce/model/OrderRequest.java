package com.e_commerce.e_commerce.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.sql.Date;

@Data
@ToString
public class OrderRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String firstName;

    private String lastName;

    private String email;

    private String mobileNo;

    private String address;

    private String city;

    private String state;

    private String pincode;

    private String paymentType;
}
