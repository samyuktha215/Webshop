package org.se.webshop.service;


import jakarta.transaction.Transactional;
import org.se.webshop.entity.*;
import org.se.webshop.repo.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class OrderService {
   @Autowired
    private OrderRepo orderRepo;
   @Autowired
   private ShoppingBasket shoppingBasket;
   @Autowired
   private UserService userService;

   public List<Order> getOrders() {
       return orderRepo.findAll();
   }
    public Order createOrderFromBasket(OrderStatus orderStatus) {
        if (shoppingBasket == null || shoppingBasket.getOrderLines().isEmpty()) {
            return null;
        }

        Order order = new Order();
        OrderStatus orderstatus= OrderStatus.valueOf(orderStatus.name());
        List<OrderLine> orderLines = shoppingBasket.getOrderLines();

        order.setOrderLines(new ArrayList<>(shoppingBasket.getOrderLines()));
        order.setStatus(orderStatus);
        order.setDate(LocalDate.now());
        order.setUser(userService.getLoggedInUser());

        orderRepo.save(order);
        shoppingBasket.clearBasket();

        return order;
    }

    @Transactional
    public boolean updateOrderStatus( OrderStatus status,long id) {
        Optional<Order> orderOptional = orderRepo.findById(id);

        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            OrderStatus orderStatus = OrderStatus.valueOf(status.name());
            order.setStatus(orderStatus);

            System.out.println("Updated Order Status for Order ID: " + id + " to " + status);
            orderRepo.save(order);
            return true;
        }

        return false;
    }

    public Order getOrderById(Long id) {
       return orderRepo.findById(id).orElse(null);
    }
}
