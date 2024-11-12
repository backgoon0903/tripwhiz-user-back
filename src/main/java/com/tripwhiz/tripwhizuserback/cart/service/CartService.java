package com.tripwhiz.tripwhizuserback.cart.service;

import com.tripwhiz.tripwhizuserback.cart.domain.Cart;
import com.tripwhiz.tripwhizuserback.cart.dto.CartListDTO;
import com.tripwhiz.tripwhizuserback.cart.repository.CartDetailsRepository;
import com.tripwhiz.tripwhizuserback.cart.repository.CartRepository;
import com.tripwhiz.tripwhizuserback.common.dto.PageRequestDTO;
import com.tripwhiz.tripwhizuserback.common.dto.PageResponseDTO;
import com.tripwhiz.tripwhizuserback.product.dto.ProductListDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final CartRepository CartRepository;

    private final CartDetailsRepository CartDetailsRepository;

    public PageResponseDTO<CartListDTO> list(PageRequestDTO pageRequestDTO) {

        log.info("2------------------------------");
        log.info(CartDetailsRepository.listOfMember(pageRequestDTO));

        return CartDetailsRepository.listOfMember(pageRequestDTO);
    }

}
