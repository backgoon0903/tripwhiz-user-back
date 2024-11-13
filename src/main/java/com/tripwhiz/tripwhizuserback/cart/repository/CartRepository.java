package com.tripwhiz.tripwhizuserback.cart.repository;

import com.tripwhiz.tripwhizuserback.cart.domain.Cart;
import com.tripwhiz.tripwhizuserback.cart.dto.CartListDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("SELECT new com.tripwhiz.tripwhizuserback.cart.dto.CartListDTO(c.bno, c.product.pno, c.qty) " +
            "FROM Cart c")
    List<CartListDTO> findAllCartItems();

}
