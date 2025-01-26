package ru.yandex.practicum.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "orders")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order {
    @Id
    @UuidGenerator
    @Column(name = "order_id")
    String orderId;
    @Column(name = "username")
    String username;
    @Column(name = "cart_id")
    String cartId;
    @Column(name = "payment_id")
    String paymentId;
    @Column(name = "delivery_id")
    String deliveryId;
    @Column(name = "state")
    String state;
    @Column(name = "delivery_weight")
    double deliveryWeight;
    @Column(name = "delivery_volume")
    double deliveryVolume;
    @Column(name = "fragile")
    boolean fragile;
    @Column(name = "total_price")
    double totalPrice;
    @Column(name = "delivery_price")
    double deliveryPrice;
    @Column(name = "product_price")
    double productPrice;
    @ToString.Exclude
    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    List<Position> positions = new ArrayList<>();
    @Column(name = "country")
    String country;
    @Column(name = "city")
    String city;
    @Column(name = "street")
    String street;
    @Column(name = "house")
    String house;
    @Column(name = "flat")
    String flat;
}
