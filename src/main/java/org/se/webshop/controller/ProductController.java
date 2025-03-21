package org.se.webshop.controller;


import org.se.webshop.entity.Order;
import org.se.webshop.entity.OrderLine;
import org.se.webshop.entity.Product;
import org.se.webshop.entity.ShoppingBasket;
import org.se.webshop.repo.OrderRepo;
import org.se.webshop.service.ProductService;
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

    @GetMapping("/products")
    public String showProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "products";
    }

    @PostMapping("/add-product")
    public String addProduct(@RequestParam String name, @RequestParam Double price) {
        productService.addProduct(name, price);
        return "redirect:/products";
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
       order.setStatus("COMPLETED");
       order.setDate(LocalDate.now());
       order.setOrderLines(shoppingBasket.getOrderLines());

       orderRepo.save(order);
       shoppingBasket.clearBasket();

       model.addAttribute("order", order);
       model.addAttribute("message", "checkoutSuccessful");
       return "order-confirmation";
    }

}
