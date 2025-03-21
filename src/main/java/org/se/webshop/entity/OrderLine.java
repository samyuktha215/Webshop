package org.se.webshop.entity;

import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private int quantity;


    public OrderLine(int quantity, Product product) {
        this.quantity = quantity;
        this.product = product;
    }


}
