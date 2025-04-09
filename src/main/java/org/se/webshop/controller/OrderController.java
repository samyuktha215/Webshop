package org.se.webshop.controller;

import jakarta.servlet.http.HttpSession;
import org.se.webshop.entity.Order;
import org.se.webshop.entity.OrderLine;
import org.se.webshop.entity.User;
import org.se.webshop.repo.UserRepo;
import org.se.webshop.service.OrderService;
import org.se.webshop.service.ShoppingBasket;
import org.se.webshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;


@Controller
public class OrderController {
    @Autowired
    private ShoppingBasket shoppingBasket;
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepo userRepo;

    public OrderController(){
    }
    @GetMapping("/checkout")
    public String showCheckoutPage(Model model) {
        model.addAttribute("shoppingBasket", shoppingBasket);
        List<OrderLine>cart=shoppingBasket.getCart();
        model.addAttribute("cart", cart);
        return "checkout";
    }

    @PostMapping("/checkout")
    public String processCheckout(Model model) {
        Order order = orderService.createOrderFromBasket();

        if (order == null) {
            model.addAttribute("error", "Your basket is empty. Please add items before checking out.");
            return "redirect:/basket";
        }
        double totalPrice=order.getOrderLines()
                .stream().mapToDouble(line->line.getProduct().getPrice() * line.getQuantity())
                .sum();

        model.addAttribute("order", order);

        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("message", "checkoutSuccessful");
        return "order-confirmation";
    }

}



