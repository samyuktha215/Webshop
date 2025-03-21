package org.se.webshop.entity;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShoppingBasket {

    List<OrderLine> orderLines;

    public ShoppingBasket() {
        orderLines = new ArrayList<>();
    }

    public void addOrderLine(OrderLine orderLine) {
        orderLines.add(orderLine);
    }

    public void removeOrderLine(OrderLine orderLine) {
        orderLines.remove(orderLine);
    }

    public double getTotalPrice() {
        double totalPrice = 0.0;
        for (OrderLine orderLine : orderLines) {
            totalPrice += orderLine.getProduct().getPrice() * orderLine.getQuantity();
        }
        return totalPrice;
    }
    public List<OrderLine> getOrderLines() {
        return orderLines;
    }
    public void clearBasket() {
        orderLines=new ArrayList<>();
    }

}
