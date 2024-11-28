package com.tripwhiz.tripwhizuserback.cart.domain;

import com.tripwhiz.tripwhizuserback.member.domain.MemberEntity;
import com.tripwhiz.tripwhizuserback.product.domain.Product;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"product", "member"})
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email", nullable = false)
    private MemberEntity member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pno", nullable = false)
    private Product product;

    @Column(nullable = false)
    private int qty;

    @Column(nullable = false)
    private boolean delFlag = false;

    public void changeQty(int qty) {
        if (qty == 0) {
            throw new IllegalArgumentException("Quantity change must not be zero.");
        }

        int newQty = this.qty + qty;

        if (newQty < 0) {
            throw new IllegalArgumentException("Quantity cannot be less than zero.");
        }

        this.qty = newQty;
    }

    public void softDelete() {
        this.delFlag = true;
    }

}
