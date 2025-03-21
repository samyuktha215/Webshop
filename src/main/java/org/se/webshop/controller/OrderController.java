package org.se.webshop.controller;


import org.se.webshop.entity.OrderLine;

import org.se.webshop.entity.ShoppingBasket;

import org.se.webshop.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/orders")
    public String getOrderDetails(Model model) {

        List<OrderLine> orderLines = orderService.getAllOrderLines();
        double totalPrice = orderService.getBasketTotal();

        model.addAttribute("orderLines", orderLines);
        model.addAttribute("totalPrice", totalPrice);
        return "orders";
    }

    @PostMapping("/orders/remove")
    public String removeOrderLine(@RequestParam("productId") int productId, @RequestParam("quantity") int quantity) {
        orderService.removeFromBasket( quantity,productId);
        return "orders";
    }

    @PostMapping("/orders/place-order")
    public String placeOrder(@RequestParam("userId") Long userId) {
        orderService.placeOrder(userId);
        return "orders";
    }

    @PostMapping("/orders/clear")
    public String clearBasket() {
        orderService.clearBasket();
        return "orders";
    }
}
