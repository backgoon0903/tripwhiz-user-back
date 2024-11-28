package com.tripwhiz.tripwhizuserback.cart.controller;

import com.tripwhiz.tripwhizuserback.cart.dto.CartListDTO;
import com.tripwhiz.tripwhizuserback.cart.dto.CartProductDTO;
import com.tripwhiz.tripwhizuserback.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@Log4j2
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/list")
    public ResponseEntity<List<CartListDTO>> list() {

        List<CartListDTO> cartItems = cartService.list();

        log.info(cartItems);

        return ResponseEntity.ok(cartItems);

    }

    // 장바구니에 물건 추가
    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestBody CartProductDTO cartProductDTO) {
        cartService.addToCart(cartProductDTO);
        return ResponseEntity.ok("Product added to cart successfully");
    }

    // 장바구니 항목 조회
//    @GetMapping("/items")
//    public ResponseEntity<List<CartProductDTO>> getCartItems() {
//        List<CartProductDTO> cartItems = cartService.getCartItems();
//        return ResponseEntity.ok(cartItems);
//    }

}