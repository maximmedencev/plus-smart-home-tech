package ru.yandex.practicum.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.entity.ShoppingCart;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, String> {

    ShoppingCart getShoppingCartByUsernameAndActive(String username, boolean active);

    @Transactional
    @Modifying
    @Query("update ShoppingCart s set s.active = :act where username = :user ")
    void setShoppingCartActive(@Param("act") boolean active, @Param("user") String username);

    @Query("select sc.cartId from ShoppingCart sc where sc.username = :user and sc.active = :act")
    String getShoppingCartIdByUsername(@Param("user") String username, @Param("act") boolean active);

}
