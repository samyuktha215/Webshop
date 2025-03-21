package org.se.webshop.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Entity
@Data

@Getter
@Setter
@NoArgsConstructor
@Table(name="products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotBlank(message = "should not be empty")
    private String name;

    private double price;

    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }




}
