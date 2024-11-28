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

    // 멤버별 장바구니 리스트 조회
    @GetMapping("/list")
    public ResponseEntity<List<CartListDTO>> list(@RequestParam("email") String email) {

        List<CartListDTO> cartItems = cartService.list(email);

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

    @DeleteMapping("delete/{pno}")
    public ResponseEntity<Void> deleteByProduct(
            @RequestParam("email") String email,
            @PathVariable("pno") Long pno) {

        cartService.softDeleteByProduct(email, pno); // Service 호출
        log.info("Product deleted successfully");
        return ResponseEntity.noContent().build(); // 204 No Content 반환
    }

    @DeleteMapping("delete/all")
    public ResponseEntity<Void> deleteAll(
            @RequestParam("email") String email) {

        cartService.softDeleteAll(email); // Service 호출
        log.info("Product deleted successfully");
        return ResponseEntity.noContent().build(); // 204 No Content 반환
    }

}