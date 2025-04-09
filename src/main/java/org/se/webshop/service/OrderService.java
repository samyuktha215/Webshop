package org.se.webshop.service;


import org.se.webshop.entity.Order;
import org.se.webshop.entity.OrderLine;
import org.se.webshop.entity.Product;
import org.se.webshop.entity.User;
import org.se.webshop.repo.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Service
public class OrderService {
   @Autowired
    private OrderRepo orderRepo;
   @Autowired
   private ShoppingBasket shoppingBasket;

   public List<Order> getOrders() {
       return orderRepo.findAll();
   }
    public Order createOrderFromBasket() {
        if (shoppingBasket == null || shoppingBasket.getOrderLines().isEmpty()) {
            return null;
        }

        Order order = new Order();
        List<OrderLine> orderLines = shoppingBasket.getOrderLines();

        order.setOrderLines(new ArrayList<>(shoppingBasket.getOrderLines()));
        order.setStatus("PENDING");
        order.setDate(LocalDate.now());

        orderRepo.save(order);
        shoppingBasket.clearBasket();

        return order;
    }

}
