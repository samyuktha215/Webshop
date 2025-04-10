package org.se.webshop.controller;

import jakarta.servlet.http.HttpSession;
import org.se.webshop.entity.Order;
import org.se.webshop.entity.OrderLine;
import org.se.webshop.entity.OrderStatus;
import org.se.webshop.entity.User;
import org.se.webshop.repo.UserRepo;
import org.se.webshop.service.OrderService;
import org.se.webshop.service.ShoppingBasket;
import org.se.webshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class OrderController {
    @Autowired
    private ShoppingBasket shoppingBasket;
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;


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
        Order order = orderService.createOrderFromBasket(OrderStatus.PENDING);

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

    @GetMapping("/orders")
    public String showOrders(Model model) {
        List<Order>orders=orderService.getOrders();
        Map<Long,Double> orderTotal=new HashMap<>();
        for(Order order:orders){
            double total=0;
            for(OrderLine orderLine:order.getOrderLines()){
                total+=orderLine.getProduct().getPrice()*orderLine.getQuantity();

            }
            orderTotal.put(order.getId(), total);
        }
        model.addAttribute("orders", orders);
        model.addAttribute("orderTotal", orderTotal);
        return "orders";
    }
    @PostMapping("/update-status")
    public String updateOrderStatus(@RequestParam("orderId") Long orderId,@RequestParam("status") String status,
                                    Model model) {

        String userRole = userService.getUserRole("VED");
        if (!userRole.equals("ADMIN")) {
            model.addAttribute("error", "You do not have permission to update the order status.");
            return "access-denied";
        }

        System.out.println("Received orderId: " + orderId);
        System.out.println("Received status: " + status);

        boolean isUpdated = orderService.updateOrderStatus(OrderStatus.DELIVERED,orderId);

        if (isUpdated) {
            model.addAttribute("message", "Order status updated successfully!");
        } else {
            model.addAttribute("error", "Order not found or update failed.");
        }

        return "orders";
    }

    @GetMapping("/order/{id}")
    public String showOrderDetails(@PathVariable Long id, Model model) {
        Order order = orderService.getOrderById(id);
        model.addAttribute("order", order);
        return "order-details";
    }



}



