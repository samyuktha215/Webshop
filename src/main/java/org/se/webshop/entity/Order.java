package org.se.webshop.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Getter
@Setter
@Table(name="orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    private LocalDate date;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    List<OrderLine> orderLines;

    public Order(OrderStatus status, LocalDate date, User user) {
        this.status = status;
        this.date = date;
        this.user = user;
    }

    public double getTotalPrice() {
        double totalPrice = 0.0;
        for (OrderLine orderLine : orderLines) {
            totalPrice += orderLine.getProduct().getPrice() * orderLine.getQuantity();
        }
        return totalPrice;
    }


    public void setTotalPrice(double totalPrice) {
    }
}
