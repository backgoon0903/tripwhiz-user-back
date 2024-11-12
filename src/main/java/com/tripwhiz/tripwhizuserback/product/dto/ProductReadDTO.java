package com.tripwhiz.tripwhizuserback.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductReadDTO {

    private Long pno;

    private String pname;

    private String pdesc;

    private int price;

    private String fileName;

}
