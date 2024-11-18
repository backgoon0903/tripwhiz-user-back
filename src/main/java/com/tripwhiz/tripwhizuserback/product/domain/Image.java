package com.tripwhiz.tripwhizuserback.product.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "product")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ino;

    private int ord;  // 고유 ID 필드로 설정
    private String fileName;  // 파일명

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_pno")
    private Product product;



    public Image(int ord, String fileName) {
        this.ord = ord;
        this.fileName = fileName;
    }


}