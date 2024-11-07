package com.tripwhiz.tripwhizuserback.order.domain;

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
@ToString
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ono;

    private Long uno;

    private int totalamount;

    @CreatedDate
    private LocalDateTime createtime = LocalDateTime.now();

    @Builder.Default
    private OrderStatus status = OrderStatus.상품준비중;


}
