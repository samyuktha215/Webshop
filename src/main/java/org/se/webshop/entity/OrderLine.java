package org.se.webshop.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Getter
@Setter
@Table(name="orderline")
public class OrderLine {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;

    public OrderLine(int quantity, Product product) {
        this.quantity = quantity;
        this.product = product;
    }

    @Override
    public String toString() {
        return "Product: " + product.getName() +
                ", Quantity: " + quantity +
                ", Price: " + product.getPrice();
    }

    public double getTotalPrice(List<OrderLine> cart) {
        double totalPrice = 0;
        for (OrderLine orderLine : cart) {
            totalPrice+=orderLine.getProduct().getPrice()*orderLine.getQuantity();
        }
        return totalPrice;

    }
}
