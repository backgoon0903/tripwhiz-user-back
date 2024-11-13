package com.tripwhiz.tripwhizuserback.cart.service;

import com.tripwhiz.tripwhizuserback.cart.dto.CartListDTO;
import com.tripwhiz.tripwhizuserback.cart.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    public List<CartListDTO> list() {

        List<CartListDTO> cartItems = cartRepository.findAllCartItems();

        return cartItems.stream()
                .map(cart -> CartListDTO.builder()
                        .bno(cart.getBno())
                        .pno(cart.getPno())
                        .qty(cart.getQty())
                        .build())
                .collect(Collectors.toList());

    }

}
