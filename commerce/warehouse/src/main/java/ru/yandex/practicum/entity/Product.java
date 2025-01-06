package ru.yandex.practicum.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "products")
public class Product {
    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    String productId;
    @Column(name = "fragile")
    Boolean fragile;
    @Column(name = "width")
    double width;
    @Column(name = "height")
    double height;
    @Column(name = "depth")
    double depth;
    @Column(name = "weight")
    double weight;
    @Column(name = "quantity")
    int quantity;


}
