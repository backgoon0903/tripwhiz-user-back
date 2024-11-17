package com.tripwhiz.tripwhizuserback.product.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ThemeCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tno;

    @Column(nullable = false)
    private String tname;

    private boolean delFlag;

    // 테마가 상품을 참조
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_pno")
    private Product product;

    // 삭제 상태 변경 메서드
//    public void changeDelFlag(boolean newDelFlag) {
//        this.delFlag = newDelFlag;
//    }
}
