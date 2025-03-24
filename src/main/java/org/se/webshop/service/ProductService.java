package org.se.webshop.service;

import org.se.webshop.entity.Category;
import org.se.webshop.entity.Product;
import org.se.webshop.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private UserService userService;
    @Autowired
    private ProductRepo productRepo;
    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    public boolean addProduct(String userName, String name, Double price, Category category) {
        System.out.println("addProduct method called");

        String role = userService.getUserRole(userName);
        System.out.println("User Role: " + role);

        if (!"ADMIN".equalsIgnoreCase(role)) {
            System.out.println("User does not have permission to add product");
            return false;
        }

        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setCategory(category);

        productRepo.save(product);
        System.out.println("Product added successfully");

        return true;
    }

    public void removeProduct(Long id) {
        productRepo.deleteById(id);
    }

    public Optional<Product> getProductById(Long productId) {
        return productRepo.findById(productId);
    }

    public Optional<Product> getProductsByCategory(Category category) {
        return productRepo.findByCategory(category);
    }
}
