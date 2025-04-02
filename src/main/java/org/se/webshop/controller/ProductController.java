package org.se.webshop.controller;


import org.se.webshop.entity.*;
import org.se.webshop.repo.OrderRepo;
import org.se.webshop.repo.ProductRepo;
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
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Controller
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
   @Autowired
   private ProductService productService;
   private final ShoppingBasket shoppingBasket = new ShoppingBasket();
    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private UserService userService;


    @GetMapping("/products")
    public String showProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "products";
    }

    @PostMapping("/products/add-product")
    public String addProduct(Model model,@RequestParam String userName,@RequestParam String name, @RequestParam Double price, @RequestParam Category category) {


            logger.info("Received request to add product by user: {}", userName);

            String role = userService.getUserRole(userName);
            logger.info("User role: {}", role);

            if (!"ADMIN".equalsIgnoreCase(role)) {
                logger.warn("User {} does not have permission to add products", userName);
                model.addAttribute("error", "Only ADMIN users can add products.");
                model.addAttribute("products", productService.getAllProducts());
                return "products";
            }

            boolean isAdded = productService.addProduct(userName, name, price, category);

            if (!isAdded) {
                logger.error("Failed to add product");
                model.addAttribute("error", "Failed to add product.");
            } else {
                logger.info("Product '{}' added successfully", name);
            }

            model.addAttribute("products", productService.getAllProducts());
            return "products";
        }


    @GetMapping("/basket")
    public String viewBasket(Model model) {
        if (shoppingBasket == null) {
            logger.warn(" ShoppingBasket is NULL!");
        } else {
            logger.info("ShoppingBasket exists.");
            logger.info(" Products in basket: " + shoppingBasket.getOrderLines().size());
        }

        List<OrderLine> orderLines = shoppingBasket.getOrderLines();
        double totalPrice = orderLines.stream()
                .mapToDouble(orderLine -> orderLine.getProduct().getPrice() * orderLine.getQuantity())
                .sum();

        model.addAttribute("orderLines", orderLines);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("basket", orderLines);

        return "basket";
    }


    @PostMapping("/add-to-basket")
    public String addToBasket(@RequestParam("productIds") List<Long> productIds) {
        for (Long productId : productIds) {
            Optional<Product> productOpt = productService.getProductById(productId);
            productOpt.ifPresent(product -> shoppingBasket.addOrderLine(new OrderLine(1, product)));
        }

        return "redirect:/basket";
    }

    @PostMapping("/remove-from-basket")
    public String removeFromBasket(@RequestParam("productId") Long productId) {
        if (shoppingBasket != null) {
            List<OrderLine> orderLines = shoppingBasket.getOrderLines();

            orderLines.removeIf(orderLine -> Objects.equals(orderLine.getProduct().getId(), productId));
            System.out.println("Removed product ID: " + productId);
        }
        return "redirect:/basket";
    }

    @PostMapping("/checkout")
    public String checkout(Model model) {
       if (shoppingBasket == null) {
           logger.warn(" ShoppingBasket is NULL first fill products and then checkout!");
           return "redirect:/basket";
       }
        logger.info("Continue Shopping");
       List<OrderLine> orderLines = shoppingBasket.getOrderLines();
       double totalPrice = shoppingBasket.getTotalPrice();

       Order order= new Order();
       order.setStatus("PENDING");
       order.setDate(LocalDate.now());
       order.setOrderLines(shoppingBasket.getOrderLines());

       orderRepo.save(order);
       shoppingBasket.clearBasket();

       model.addAttribute("order", order);
       model.addAttribute("message", "checkoutSuccessful");
       return "order-confirmation";
    }

}
