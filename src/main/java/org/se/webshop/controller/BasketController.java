package org.se.webshop.controller;

import org.se.webshop.entity.OrderLine;
import org.se.webshop.entity.Product;
import org.se.webshop.service.ProductService;
import org.se.webshop.service.ShoppingBasket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Controller

public class BasketController {
    @Autowired
    private ShoppingBasket shoppingBasket;
    @Autowired
    private ProductService productService;

    // Add to Cart
    @PostMapping("/basket/add")
    public String addToCart(@RequestParam Long productId, @RequestParam int quantity, Model model) {
        shoppingBasket.addOrderLine(productId, quantity);
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        model.addAttribute("cart", shoppingBasket.getCart());
        double totalPrice = shoppingBasket.calculateTotalPrice(shoppingBasket.getCart());
        model.addAttribute("totalPrice", totalPrice);
        return "products";
    }

    // Update Cart Method
    @PostMapping("/basket/update")
    public String updateCart(@RequestParam List<Long> productIds, @RequestParam List<Integer> quantities, Model model) {
        List<OrderLine> updatedCart = shoppingBasket.updateCart(productIds, quantities);
        model.addAttribute("cart", updatedCart);
        double totalPrice = shoppingBasket.calculateTotalPrice(updatedCart);
        model.addAttribute("totalPrice", totalPrice);
        return "basket";
    }

    // Clear the Cart
    @PostMapping("/basket/clear")
    public String clearBasket(Model model) {
        shoppingBasket.clearBasket();
        model.addAttribute("cart", new ArrayList<>());
        model.addAttribute("totalPrice", 0.0);
        return "basket";
    }

    @PostMapping("/basket/remove")
    public String removeFromCart(@RequestParam Long productId) {
        shoppingBasket.removeItemFromCart(productId);
        return "redirect:/basket";
    }

    @GetMapping("/basket")
    public String viewCart(Model model) {
        List<OrderLine> cart = shoppingBasket.getCart();

        if (cart == null) {
            cart = new ArrayList<>();
        }
        double totalPrice = shoppingBasket.calculateTotalPrice(cart);

        model.addAttribute("cart", cart);
        model.addAttribute("totalPrice", totalPrice);

        return "basket";
    }

}
