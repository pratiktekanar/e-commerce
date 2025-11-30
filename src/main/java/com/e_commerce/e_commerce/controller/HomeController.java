package com.e_commerce.e_commerce.controller;

import com.e_commerce.e_commerce.model.Category;
import com.e_commerce.e_commerce.model.Product;
import com.e_commerce.e_commerce.model.UserDtls;
import com.e_commerce.e_commerce.service.CategoryService;
import com.e_commerce.e_commerce.service.ProductService;
import com.e_commerce.e_commerce.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;


@Controller

public class HomeController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @ModelAttribute
    public void getUserDetails(Principal p, Model m){
        if(p != null){
            String email = p.getName();
            UserDtls userDtls = userService.getUserByEmail(email);
            m.addAttribute("user",userDtls);
        }

        List<Category> allActiveCategory = categoryService.getAllActiveCategory();
        m.addAttribute("categorys",allActiveCategory);
    }

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/register")
    public String register(){
        return "register";
    }

    @GetMapping("/signin")
    public String login(){
        return "login";
    }

    @GetMapping("/products")
    public String products(Model m , @RequestParam(value="category" , defaultValue = "") String category) {
        List<Category> categories = categoryService.getAllActiveCategory();
        List<Product> products = productService.getAllActivProducts(category);

        m.addAttribute("categories",categories);
        m.addAttribute("products",products);
        m.addAttribute("paramValue",category);

        return "product";
    }

    @GetMapping("/product/{id}")
    public String product(@PathVariable int id, Model m){
        Product productById = productService.getProductById(id);
        m.addAttribute("product",productById);
        return "view_product";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute UserDtls user, @RequestParam("img") MultipartFile file, HttpSession session) throws IOException {

        String imgName = file.isEmpty() ? "default.jpg" : file.getOriginalFilename();
        user.setProfileImage(imgName);
        userService.saveUser(user);
        if(!ObjectUtils.isEmpty(user)){
            if(!file.isEmpty()){
                File saveFile = new ClassPathResource("static/img").getFile();

                // add profile_img subfolder
                Path dirPath = Paths.get(saveFile.getAbsolutePath(), "profile_img");

                // ✅ create folder if missing
                if (!Files.exists(dirPath)) {
                    Files.createDirectories(dirPath);
                }

                // final file path
                Path path = dirPath.resolve(file.getOriginalFilename());
                Files.copy(file.getInputStream(),path , StandardCopyOption.REPLACE_EXISTING);
            }
            session.setAttribute("SuccMsg","Saved successfully ");
        }else{
            session.setAttribute("errorMsg","Something wrong on server");
        }
        return "redirect:/register";
    }
}
