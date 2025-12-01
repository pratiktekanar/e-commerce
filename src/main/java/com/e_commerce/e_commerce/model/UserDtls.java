package com.e_commerce.e_commerce.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.web.WebProperties;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class UserDtls {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String MobileNumber ;
    private String email;
    private String address;
    private String city;
    private String state;
    private String pincode;
    private String password;
    private String profileImage;
    private String role;
    private String resetToken;
    private Boolean isEnable;
    private Boolean accountNonLocked;
    private Integer failedAttempt;
    private Date lockTime;

}
