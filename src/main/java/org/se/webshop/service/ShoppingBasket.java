package org.se.webshop.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.se.webshop.entity.OrderLine;
import org.se.webshop.entity.Product;
import org.se.webshop.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
public class ShoppingBasket {
    @Autowired
    public ProductRepo productRepo;

    @Getter
    private List<OrderLine> orderLines;

    public ShoppingBasket() {
        orderLines = new ArrayList<>();
    }


    public void addOrderLine(Long productId, int quantity) {
        Product product = productRepo.findById(productId).orElse(null);
        if (product != null) {
            Optional<OrderLine> existingOrderLine = orderLines.stream()
                    .filter(line -> line.getProduct().getId()==(productId))
                    .findFirst();

            if (existingOrderLine.isPresent()) {
                OrderLine line = existingOrderLine.get();
                line.setQuantity(line.getQuantity() + quantity);
            } else {
                orderLines.add(new OrderLine(quantity, product));
            }
        }
    }



    public List<OrderLine> getCart() {
        return orderLines;
    }


    public void removeItemFromCart(Long productId) {
        orderLines.removeIf(item -> item.getProduct().getId()==(productId));
    }

    public List<OrderLine> updateCart(List<Long> productIds, List<Integer> quantities) {
        List<OrderLine> cart = getCart();
        for (int i = 0; i < productIds.size(); i++) {
            Long productId = productIds.get(i);
            int quantity = quantities.get(i);

            for (OrderLine orderLine : cart) {
                if (orderLine.getProduct().getId()==(productId)) {

                    orderLine.setQuantity(quantity);
                    break;
                }
            }
        }
        return cart;
    }

    public double calculateTotalPrice(List<OrderLine>cart) {
        double totalPrice = 0;
        for (OrderLine orderLine : cart) {
            totalPrice += orderLine.getProduct().getPrice() * orderLine.getQuantity();
        }
        return totalPrice;
    }


    public void clearBasket() {
        orderLines.clear();
    }
}
