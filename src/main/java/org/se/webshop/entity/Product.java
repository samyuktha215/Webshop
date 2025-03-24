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

    @Enumerated(EnumType.STRING)
    private Category category;

    public Product(String name, double price,Category category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

}
