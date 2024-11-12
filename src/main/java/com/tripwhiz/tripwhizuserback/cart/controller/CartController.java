package com.tripwhiz.tripwhizuserback.cart.controller;

import com.tripwhiz.tripwhizuserback.cart.domain.Cart;
import com.tripwhiz.tripwhizuserback.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/list")
    public ResponseEntity<List<Cart>> getCartsByBno(@RequestParam Long bno) {

        log.info(cartService.getCarts(bno));

        List<Cart> carts = cartService.getCarts(bno);

        return ResponseEntity.ok(carts);

    }

}
