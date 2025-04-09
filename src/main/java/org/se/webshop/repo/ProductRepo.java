package org.se.webshop.repo;


import org.se.webshop.entity.Category;
import org.se.webshop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
    Product findById(long id);

    Optional<Product> findByCategory(Category category);

    Product getProductById(Long productId);

    List<Product> searchProductByName(String productName);
}
