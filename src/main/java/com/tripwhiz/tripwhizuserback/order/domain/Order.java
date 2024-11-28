package com.tripwhiz.tripwhizuserback.order.domain;

import com.tripwhiz.tripwhizuserback.member.domain.MemberEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ono;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email", nullable = false)
    private MemberEntity member;

    @Column(nullable = false)
    private int totalAmount;

    @Column(nullable = false)
    private int totalPrice;

    @CreatedDate
    @Builder.Default
    private LocalDateTime createtime = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime pickupdate;

    @Builder.Default
    @Column(nullable = false, columnDefinition = "varchar(50) default 'PREPARING'")
    private OrderStatus status = OrderStatus.PREPARING;

    @Builder.Default
    @Column(nullable = false)
    private boolean delFlag = false;

}
