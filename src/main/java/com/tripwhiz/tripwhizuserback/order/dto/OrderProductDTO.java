package com.tripwhiz.tripwhizuserback.order.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderProductDTO {
    private Long pno; // Product의 pno
    private int amount; // 수량
    private double price; // 가격
}
