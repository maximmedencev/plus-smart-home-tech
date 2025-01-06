package ru.yandex.practicum.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@ToString
@Table(name = "carts_positions")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Position {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    int id;
    @Column(name = "product_id")
    String productId;
    @ManyToOne
    @JoinColumn(name = "cart_id")
    ShoppingCart cart;
    @Column(name = "quantity")
    int quantity;
}
