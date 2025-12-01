package com.e_commerce.e_commerce.controller;

import com.e_commerce.e_commerce.model.Category;
import com.e_commerce.e_commerce.model.Product;
import com.e_commerce.e_commerce.model.UserDtls;
import com.e_commerce.e_commerce.service.CartService;
import com.e_commerce.e_commerce.service.CategoryService;
import com.e_commerce.e_commerce.service.ProductService;
import com.e_commerce.e_commerce.service.UserService;
import com.e_commerce.e_commerce.util.CommonUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.UUID;


@Controller

public class HomeController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private CartService cartService;

    @ModelAttribute
    public void getUserDetails(Principal p, Model m){
        if(p != null){
            String email = p.getName();
            UserDtls userDtls = userService.getUserByEmail(email);
            m.addAttribute("user",userDtls);
            Integer countCart = cartService.getCountCard(userDtls.getId());
            m.addAttribute("countCart" , countCart);
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

    // -----***Forget password logic***-----

    @GetMapping("/forgot-password")
    public String showForgetPassword()
    {
        return "forgot_password.html";
    }

    @PostMapping("/forgot-password")
    public String processForgetPassword(@RequestParam String email, HttpSession session, HttpServletRequest request)
            throws UnsupportedEncodingException, MessagingException
    {
       UserDtls userByEmail = userService.getUserByEmail(email);
       if(ObjectUtils.isEmpty(userByEmail))
       {
           session.setAttribute("errorMsg","invalid email");
       }else{
           String resetToken = UUID.randomUUID().toString();
           userService.updateUserResetToken(email , resetToken);

           // Generate URL : http://localhost:8080/forgot-password?token=rgergegfdsvdgrtfvdfb
           String url = CommonUtil.generateUrl(request)+"/reset-password?token="+resetToken;

           Boolean sendEmail = commonUtil.sendEmail(url,email);
           if(sendEmail)
           {
               session.setAttribute("succMsg","Please check your email... Password reset link send");
           }else{
               session.setAttribute("errorMsg","Something wrong on server ! Email not send");
           }
       }
        return "redirect:/forgot-password";
    }


    @GetMapping("/reset-password")
    public String showResetPassword(@RequestParam String token,HttpSession session,Model m)
    {
        UserDtls userByToken = userService.getUserByResetToken(token);
        if(ObjectUtils.isEmpty(userByToken)){
            m.addAttribute("msg","You link is invalid or expired");
            return "message";
        }
        m.addAttribute("token",token);
        return "reset_password";
    }

    @PostMapping("/reset-password")
    public String showResetPassword(@RequestParam String token,@RequestParam String password,Model m)
    {
        UserDtls userByToken = userService.getUserByResetToken(token);
        if(ObjectUtils.isEmpty(userByToken)){
            m.addAttribute("msg","You link is invalid or expired");
            return "message";
        }else
        {
            userByToken.setPassword(passwordEncoder.encode(password));
            userByToken.setResetToken(null);
            userService.updateUser(userByToken);

            m.addAttribute("msg","Password change successfully");
            return "message";
        }

    }
}
