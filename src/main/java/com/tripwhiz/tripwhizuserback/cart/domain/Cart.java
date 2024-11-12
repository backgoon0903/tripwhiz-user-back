package com.tripwhiz.tripwhizuserback.cart.domain;

import com.tripwhiz.tripwhizuserback.member.domain.MemberEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    @OneToOne
    private MemberEntity member;

}
