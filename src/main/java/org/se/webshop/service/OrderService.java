package org.se.webshop.service;


import org.se.webshop.entity.*;


import org.se.webshop.repo.OrderRepo;
import org.se.webshop.repo.ProductRepo;
import org.se.webshop.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class OrderService {

    private final ProductRepo productRepo;
    private final UserRepo userRepo;
    private final OrderRepo orderRepo;
    private  ShoppingBasket shoppingBasket=new ShoppingBasket();

    public OrderService(ProductRepo productRepo, UserRepo userRepo, OrderRepo orderRepo) {
        this.productRepo = productRepo;
        this.userRepo = userRepo;
        this.orderRepo = orderRepo;

    }

    public List<OrderLine> getAllOrderLines() {
        return shoppingBasket.getOrderLines();
    }

    public double getBasketTotal() {
        return shoppingBasket.getTotalPrice();
    }

    public void removeFromBasket(int productId, int quantity) {
        Product product = productRepo.findById(productId).orElse(null);
        if (product != null) {
            OrderLine orderLine = new OrderLine(quantity, product);
            shoppingBasket.removeOrderLine(orderLine);
        }
    }

    public void placeOrder(Long userId) {

        User user = userRepo.findById(userId).orElse(null);
        if (user != null) {
            Order order = new Order();
            order.setUser(user);
            order.setStatus("PENDING");
            orderRepo.save(order);
        }
    }

    public void clearBasket() {
        shoppingBasket.clearBasket();
    }
}
