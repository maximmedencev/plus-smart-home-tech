package ru.yandex.practicum.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;

@Getter
@Setter
@ToString
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "addresses")
public class Address {
    @Id
    @UuidGenerator
    @Column(name = "address_id")
    String addressId;
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
