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
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "product_id")
    String productId;
    @Column(name = "product_name")
    String productName;
    @Column(name = "description")
    String description;
    @Column(name = "image_src")
    String imageSrc;
    @Column(name = "quantity_state")
    String quantityState;
    @Column(name = "product_state")
    String productState;
    @Column(name = "rating")
    Double rating;
    @Column(name = "product_category")
    String productCategory;
    @Column(name = "price")
    Double price;
}
