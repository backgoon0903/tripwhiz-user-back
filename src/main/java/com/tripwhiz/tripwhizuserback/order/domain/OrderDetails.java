package com.tripwhiz.tripwhizuserback.order.domain;

import com.tripwhiz.tripwhizuserback.product.domain.Product;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_details")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrderDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long odno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ono", nullable = false)
    private Order order; // Order와 다대일 관계

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pno", nullable = false)
    private Product product; // Product와 다대일 관계

    @Column(nullable = false)
    private int amount; // 주문 수량

    @Column(nullable = false)
    private double price; // 해당 상품의 총 가격 (단가 * 수량)

    @Column(nullable = true)
    private String qrCodePath; // QR 코드 파일명 (주문 항목별로 저장)
}
