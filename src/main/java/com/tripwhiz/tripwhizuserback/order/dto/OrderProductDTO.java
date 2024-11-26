package com.tripwhiz.tripwhizuserback.order.dto;

import com.tripwhiz.tripwhizuserback.product.dto.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderProductDTO {

    private ProductDTO product;
    private int amount;

}
