package com.tripwhiz.tripwhizuserback.cart.service;

import com.tripwhiz.tripwhizuserback.cart.domain.Cart;
import com.tripwhiz.tripwhizuserback.cart.dto.CartListDTO;
import com.tripwhiz.tripwhizuserback.cart.dto.CartProductDTO;
import com.tripwhiz.tripwhizuserback.cart.repository.CartRepository;
import com.tripwhiz.tripwhizuserback.product.domain.Product;
import com.tripwhiz.tripwhizuserback.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    // 장바구니에 물건 추가
    public void addToCart(CartProductDTO cartProductDTO) {
        // 제품 ID로 제품을 검색
        Product product = productRepository.findById(cartProductDTO.getPno())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // 장바구니에서 해당 제품 찾기
        Optional<Cart> existingCart = cartRepository.findByProduct(cartProductDTO.getPno());

        if (existingCart.isPresent()) {
            // 기존 제품이 있으면 수량 업데이트
            Cart cart = existingCart.get();
            cart.changeQty(cartProductDTO.getQty());
        } else {
            // 없으면 새로 추가
            Cart cart = Cart.builder()
                    .product(product)
                    .qty(cartProductDTO.getQty())
                    .build();
            cartRepository.save(cart);
        }
    }

    // 장바구니 목록 조회
//    public List<CartProductDTO> getCartItems() {
//        return cartRepository.findAllCartItems();
//    }

    public List<CartListDTO> list() {

        List<CartListDTO> cartItems = cartRepository.findAllCartItems();

        return cartItems.stream()
                .map(cart -> CartListDTO.builder()
                        .email(cart.getEmail())
                        .bno(cart.getBno())
                        .pno(cart.getPno())
                        .qty(cart.getQty())
                        .build())
                .collect(Collectors.toList());

    }

}
