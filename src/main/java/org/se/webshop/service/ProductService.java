package org.se.webshop.service;

import org.se.webshop.entity.Product;
import org.se.webshop.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepo productRepo;
    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    public void addProduct(String name, Double price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        productRepo.save(product);
    }

    public void removeProduct(Long id) {
        productRepo.deleteById(id);
    }

    public Optional<Product> getProductById(Long productId) {
        return productRepo.findById(productId);
    }
}
