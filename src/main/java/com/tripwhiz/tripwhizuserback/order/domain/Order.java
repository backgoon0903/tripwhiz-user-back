package com.tripwhiz.tripwhizuserback.order.domain;

import com.tripwhiz.tripwhizuserback.member.domain.MemberEntity;
import com.tripwhiz.tripwhizuserback.store.domain.Spot;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spno", nullable = false)
    private Spot spot;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetails> orderDetails;

    @Column(nullable = false)
    private int totalAmount;

    @Column(nullable = false)
    private int totalPrice;

    @CreatedDate
    @Builder.Default
    private LocalDateTime createtime = LocalDateTime.now();

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime pickupdate = LocalDateTime.now();

    @Builder.Default
    @Column(nullable = false, columnDefinition = "varchar(50) default 'PREPARING'")
    private OrderStatus status = OrderStatus.PREPARING;

    @Builder.Default
    @Column(nullable = false)
    private boolean delFlag = false;

    @Column(nullable = true)
    private LocalDateTime statusChangeTime;

    @Column(nullable = true)
    private String userFcmToken; // 유저 FCM 토큰 저장

    public void changeStatus(OrderStatus newStatus) {
        validateStatusChange(newStatus);
        this.status = newStatus;
        this.statusChangeTime = LocalDateTime.now();
    }

    private void validateStatusChange(OrderStatus newStatus) {
        if (this.status == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Cancelled orders cannot be modified.");
        }
        if (this.status == OrderStatus.PREPARING && newStatus != OrderStatus.APPROVED) {
            throw new IllegalArgumentException("Invalid status transition from PREPARING.");
        }
    }
}

