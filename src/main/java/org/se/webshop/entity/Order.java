package org.se.webshop.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

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

    private String status;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    List<OrderLine> orderLines;

    public Order(String status, LocalDate date, User user) {
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
