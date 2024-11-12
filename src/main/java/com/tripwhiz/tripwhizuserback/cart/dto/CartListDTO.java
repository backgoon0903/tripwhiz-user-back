package com.tripwhiz.tripwhizuserback.cart.dto;

import lombok.Data;

@Data
public class CartListDTO {

    private Long pno;
    private String pname;
    private int price;
    private String fileName;
    private int qty;

    public CartListDTO(Long pno, String pname, int price, String fileName, int qty) {
        this.pno = pno;
        this.pname = pname;
        this.price = price;
        this.fileName = fileName;
        this.qty = qty;
    }
}
