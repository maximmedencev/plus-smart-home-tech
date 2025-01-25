package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.entity.Address;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, String> {
    Optional<Address> findByCountryAndCityAndStreetAndHouseAndFlat(String country,
                                                                   String city,
                                                                   String street,
                                                                   String house,
                                                                   String flat);
}