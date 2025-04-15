package org.se.webshop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.se.webshop.entity.Order;
import org.se.webshop.entity.OrderLine;
import org.se.webshop.entity.Product;
import org.se.webshop.repo.ProductRepo;
import org.se.webshop.service.ShoppingBasket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class WebshopApplicationTests {
    @Autowired
    private ShoppingBasket shoppingBasket;
    @Autowired
    private ProductRepo productRepo;

    @BeforeEach
    void setUp() {

       shoppingBasket.clearBasket();
       if(!productRepo.existsById(1L)) {
           Product product = new Product();
           product.setId(1L);
           product.setName("test");
           product.setPrice(30);
           productRepo.save(product);
       }
    }


    @Test
    public void testShoppingBasket() {
       shoppingBasket.addOrderLine(1L, 2);

       List<OrderLine> cart = shoppingBasket.getCart();
       assertEquals(1, cart.size());
       assertEquals(2, cart.get(0).getQuantity());
       assertEquals("apple", cart.get(0).getProduct().getName());

   }
    @Test
    void testCalculateTotalPrice() {
        shoppingBasket.addOrderLine(1L, 3);
        List<OrderLine> cart = shoppingBasket.getCart();
        double total = shoppingBasket.calculateTotalPrice(cart);
        assertEquals(90, total, 0.01);
    }

}
