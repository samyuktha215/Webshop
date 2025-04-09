package org.se.webshop.controller;


import org.se.webshop.entity.*;
import org.se.webshop.repo.OrderRepo;
import org.se.webshop.service.ProductService;
import org.se.webshop.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDate;
import java.util.*;


@Controller
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
   @Autowired
   private ProductService productService;

    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private UserService userService;


    @GetMapping("/products")
    public String showProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        List<Long> selectedProducts = new ArrayList<>();
        model.addAttribute("selectedProducts", selectedProducts);
        return "products";
    }

    @GetMapping("/products/add-product")
    public String showAddProductPage(Model model) {
        return "add-product";
    }

    @PostMapping("/products/add-product")
    public String addProduct(Model model,
                             @RequestParam String name,
                             @RequestParam Double price,
                             @RequestParam Category category,
                             @RequestParam("action") String action) {

        String userName = "sam";

        boolean isAdded = productService.addProduct(userName, name, price, category);

        if (!isAdded) {
            model.addAttribute("error", "Failed to add product. Only ADMIN users can add products.");
        } else {
            model.addAttribute("message", "Product added successfully");
        }

        model.addAttribute("products", productService.getAllProducts());
        if ("add".equals(action)) {
            // Redirect to the product listing page after adding
            return "redirect:/products";
        } else if ("continue".equals(action)) {
            // Stay on the same page to add another product
            return "redirect:/products/add-product";
        }
        return "products";
    }

    @PostMapping("/search")
    public String searchProducts(Model model, @RequestParam String productName) {
        model.addAttribute("products", productService.searchProducts(productName));
        return "products";
    }


}
