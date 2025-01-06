package ru.yandex.practicum.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Table(name = "carts")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "cart_id")
    String cartId;
    @Column(name = "username")
    String username;
    @OneToMany
    @JoinColumn(name = "cart_id")
    List<Position> positions;
    @Column(name = "active")
    boolean active;
}
