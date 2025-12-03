package com.e_commerce.e_commerce.controller;

import com.e_commerce.e_commerce.model.Cart;
import com.e_commerce.e_commerce.model.Category;
import com.e_commerce.e_commerce.model.OrderRequest;
import com.e_commerce.e_commerce.model.UserDtls;
import com.e_commerce.e_commerce.service.CartService;
import com.e_commerce.e_commerce.service.CategoryService;
import com.e_commerce.e_commerce.service.OrderService;
import com.e_commerce.e_commerce.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequestMapping("/user")
@Controller
public class UserController {

    @Autowired
    private UserService  userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/")
    public String home(){
        return "user/home";
    }

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

    @GetMapping("/addCart")
    public String addToCart(@RequestParam Integer pid, @RequestParam Integer uid,HttpSession session) {
        Cart saveCart = cartService.saveCart(pid, uid);

        if (ObjectUtils.isEmpty(saveCart)) {
            session.setAttribute("errorMsg", "Product add to cart failed");
        }else {
            session.setAttribute("succMsg", "Product added to cart");
        }
        return "redirect:/product/" + pid;
    }

    @GetMapping("/cart")
    public String loadCartPage(Principal p,Model m)
    {
        String email = p.getName();
        UserDtls user = getLoggedInDetails(p);
        List<Cart> carts = cartService.getCartsByUser(user.getId());
        m.addAttribute("carts",carts);
        if(carts.size()>0){
        m.addAttribute("totalOrderPrice",carts.get(carts.size()-1).getTotalOrderPrice());}
        return "/user/cart";
    }

    @GetMapping("/cartQuantityUpdate")
    public  String updateCartQuantity(@RequestParam String sy,@RequestParam Integer cid)
    {
        cartService.updateQuantity(sy,cid);
        return "redirect:/user/cart";
    }

    @GetMapping("/orders")
    public String orderPage()
    {
        return "/user/order";
    }

    @PostMapping("/save-order")
    public String saveOrder(@ModelAttribute OrderRequest request,Principal p)
    {
        UserDtls user = getLoggedInDetails(p);
        orderService.saveOrder(user.getId(), request);
        return "/user/success";
    }

    private UserDtls getLoggedInDetails(Principal p)
    {
        String email = p.getName();
        UserDtls userDtls = userService.getUserByEmail(email);
        return userDtls;
    }

}


