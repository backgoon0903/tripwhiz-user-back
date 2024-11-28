package com.tripwhiz.tripwhizuserback.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartProductDTO {

    private Long pno;
    private int qty; // 수량

}