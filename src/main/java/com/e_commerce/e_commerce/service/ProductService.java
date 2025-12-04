package com.e_commerce.e_commerce.service;

import com.e_commerce.e_commerce.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    public Product saveProduct(Product product);

    public List<Product> getAllProducts();

    public Boolean deleteProduct(Integer id);

    public Product getProductById(Integer id);

    public Product updateProduct(Product product, MultipartFile image);

    public List<Product> getAllActivProducts(String category);

    public List<Product> searchProduct(String ch);

}
