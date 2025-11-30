package com.e_commerce.e_commerce.controller;

import com.e_commerce.e_commerce.model.Category;
import com.e_commerce.e_commerce.model.Product;
import com.e_commerce.e_commerce.model.UserDtls;
import com.e_commerce.e_commerce.service.CategoryService;
import com.e_commerce.e_commerce.service.ProductService;
import com.e_commerce.e_commerce.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
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
@RequestMapping("/admin")

public class AdminController {

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
        return "admin/index";
    }

    @GetMapping("/loadAddProduct")
    public String loadAddProduct(Model m){
        List<Category> categories = categoryService.getAllCategory();
        m.addAttribute("categories",categories);
        return "admin/add_product";
    }


    @GetMapping("/category")
    public String category(Model m){
        m.addAttribute("categorys",categoryService.getAllCategory());
        return "admin/category";
    }

    @PostMapping("/saveCategory")
    public String saveCategory(@ModelAttribute Category category , @RequestParam("file") MultipartFile file, HttpSession session){

        String imageName = (file != null) ? file.getOriginalFilename() : "default.jpg";

        category.setImageName(imageName);
        Boolean existsCategory = categoryService.existsCategory(category.getName());

        if(existsCategory){
            session.setAttribute("ErrorMsg" , "Category Name already exists");
        }else{
            Category saveCategory = categoryService.saveCategory(category);
            if(ObjectUtils.isEmpty(saveCategory)){
                session.setAttribute("ErrorMsg" , "Not saved ! Internal server Error");
            }else{

                try {
                    File saveFile = new ClassPathResource("static/img").getFile();
                    // Ensure the directory exists
                    File categoryImgDir = new File(saveFile, "category_img");
                    if (!categoryImgDir.exists()) categoryImgDir.mkdirs();

                    Path path = Paths.get(categoryImgDir.getAbsolutePath(), file.getOriginalFilename());
                    Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                    session.setAttribute("succMsg", "Saved Successfully");
                } catch (Exception e) {
                    e.printStackTrace();
                    session.setAttribute("ErrorMsg", "File upload failed: " + e.getMessage());
                }
            }
        }
        return "redirect:/admin/category";
    }

    @GetMapping("/deleteCategory/{id}")
    public String deleteCategory(@PathVariable int id, HttpSession session){
        Boolean deleteCategory = categoryService.deleteCategory(id);

        if(deleteCategory){
            session.setAttribute("succMsg","Category deleted success");
        }else{
            session.setAttribute("ErrorMsg","Somthing wrong on server");
        }
        return "redirect:/admin/category";
    }

    @GetMapping("/loadEditCategory/{id}")
    public String loadEditCategory(@PathVariable int id,Model m,HttpSession session){
        Category category=categoryService.getCategoryById(id);
        m.addAttribute("category",category)  ;

        return "admin/edit_category";
    }

    @PostMapping("/updateCategory")
    public String updateCategory(@ModelAttribute Category category ,@RequestParam("file") MultipartFile file ,
                                 HttpSession session){
        Category oldCategory = categoryService.getCategoryById(category.getId());
        String imageName = file.isEmpty() ? oldCategory.getImageName() : file.getOriginalFilename();
        if(!ObjectUtils.isEmpty(category)){
            oldCategory.setName(category.getName());
            oldCategory.setIsActive(category.getIsActive());
            oldCategory.setImageName(imageName);
        }
        Category updateCategory = categoryService.saveCategory(oldCategory);
        if(!ObjectUtils.isEmpty(updateCategory)){
            session.setAttribute("succMsg","Category update success");
        }else{
            session.setAttribute("errorMsg","Something wrong on server");
        }
        return "redirect:/admin/loadEditCategory/"+category.getId();
    }

    @PostMapping("/saveProduct")
    public String saveProduct(@ModelAttribute Product product, @RequestParam("file") MultipartFile image,
                              HttpSession session) throws IOException {

        String imageName = image.isEmpty() ? "default.jpg" : image.getOriginalFilename();

        product.setImage(imageName);

        product.setDiscount(0);

        product.setDiscountPrice(product.getPrice());

        Product saveProduct = productService.saveProduct(product);

        if (!ObjectUtils.isEmpty(saveProduct)) {

            File saveFile = new ClassPathResource("static/img").getFile();

            Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "product_img" + File.separator
                    + image.getOriginalFilename());

            System.out.println(path);
            Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            session.setAttribute("succMsg", "Product Saved Success");
        } else {
            session.setAttribute("errorMsg", "something wrong on server");
        }

        return "redirect:/admin/loadAddProduct";
    }

    @GetMapping("/products")
    public String loadViewProduct(Model m){
        m.addAttribute("products",productService.getAllProducts());
        return "admin/products";
    }

    @GetMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable int id,HttpSession session){
        Boolean deleteProduct = productService.deleteProduct(id);
        if(deleteProduct){
            session.setAttribute("succMsg","Product delete success");
        }else{
            session.setAttribute("errorMsg","Something wrong on server");
        }
        return "redirect:/admin/products";
    }

    @GetMapping("/editProduct/{id}")
    public String editProduct(@PathVariable int id,Model m){
        m.addAttribute("product",productService.getProductById(id));
        m.addAttribute("categories",categoryService.getAllCategory());
        return "/admin/edit_product";
    }

    @PostMapping("/updateProduct")
    public String updateProduct(@ModelAttribute Product product,@RequestParam("file") MultipartFile image,HttpSession session){
        Product updateProduct = productService.updateProduct(product,image);
        if(product.getDiscount()<0 || product.getDiscount()>100){
            session.setAttribute("errorMsg","Invalid Discount");
        }else{
            if(!ObjectUtils.isEmpty(updateProduct)){
                session.setAttribute("succMsg","Product updated success");
            }else{
                session.setAttribute("errorMsg","Something wrong on server");
            }
        }

        return "redirect:/admin/editProduct/"+product.getId();
    }

    @GetMapping("/users")
    public String getAllUsers(Model m)
    {
        List<UserDtls> users = userService.getUsers("ROLE_USER");
        m.addAttribute("users",users);
        return "/admin/users";
    }

    @GetMapping("/updateSts")
    public String updateUserAccountStatus(@RequestParam Boolean status,@RequestParam Integer id,HttpSession session)
    {
        Boolean f = userService.updateUserAccountStatus(id,status);
        if(f)
        {
            session.setAttribute("succMsg","Account Status Updated");
        }else
        {
            session.setAttribute("errorMsg","Something wrong on server");
        }
        return "redirect:/admin/users";
    }

}
