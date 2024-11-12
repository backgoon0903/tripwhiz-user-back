package com.tripwhiz.tripwhizuserback.cart.repository;

import com.tripwhiz.tripwhizuserback.cart.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {



}