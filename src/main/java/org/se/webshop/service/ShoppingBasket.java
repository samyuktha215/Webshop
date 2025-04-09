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
    private ProductRepo productRepo;

    @Getter
    private List<OrderLine> orderLines;

    public ShoppingBasket() {
        orderLines = new ArrayList<>();
    }

    // Add product to cart
    public void addOrderLine(Long productId, int quantity) {
        // Retrieve the product by its ID
        Product product = productRepo.findById(productId).orElse(null);

        if (product != null) {
            // Check if the product is already in the cart
            Optional<OrderLine> existingOrderLine = orderLines.stream()
                    .filter(line -> line.getProduct().getId()==(productId))
                    .findFirst();

            if (existingOrderLine.isPresent()) {
                // If the product already exists in the cart, update the quantity
                OrderLine line = existingOrderLine.get();
                line.setQuantity(line.getQuantity() + quantity);
            } else {
                // Otherwise, add a new product to the cart
                orderLines.add(new OrderLine(quantity, product));
            }
        }
    }


    // Get the current cart (list of order lines)
    public List<OrderLine> getCart() {
        return orderLines;
    }

    // Remove an item from the cart
    public void removeItemFromCart(Long productId) {
        orderLines.removeIf(item -> item.getProduct().getId()==(productId));
    }

    public List<OrderLine> updateCart(List<Long> productIds, List<Integer> quantities) {
        List<OrderLine> cart = getCart(); // Get the current cart
        for (int i = 0; i < productIds.size(); i++) {
            Long productId = productIds.get(i);
            int quantity = quantities.get(i);

            // Find the matching order line for the productId
            for (OrderLine orderLine : cart) {
                if (orderLine.getProduct().getId()==(productId)) {
                    // Update the quantity for the matching item
                    orderLine.setQuantity(quantity);
                    break;
                }
            }
        }
        return cart;
    }
    // You can also add additional methods like calculateTotalPrice or others if needed
    public double calculateTotalPrice(List<OrderLine>cart) {
        double totalPrice = 0;
        for (OrderLine orderLine : cart) {
            totalPrice += orderLine.getProduct().getPrice() * orderLine.getQuantity();
        }
        return totalPrice;
    }


    // Clear the cart
    public void clearBasket() {
        orderLines.clear();
    }
}
