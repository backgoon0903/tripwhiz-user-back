package com.tripwhiz.tripwhizuserback.payment.dto;

import com.tripwhiz.tripwhizuserback.payment.domain.PayType;  // 결제수단을 나타내는 PayType 열거형 클래스
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

// Lombok을 사용해 자동으로 게터와 세터 메서드를 생성합니다.
@Getter
@Setter
@Builder
public class PaymentReqDTO {
    // 결제수단을 나타내는 필드입니다. PayType 열거형을 사용하여 결제 방식(예: 카드, 계좌이체 등)을 지정합니다.
    private PayType PayType;

    // 결제 금액을 저장하는 필드입니다.
    private Integer price;

    // 주문 이름을 저장하는 필드로, 상품명이나 결제와 관련된 이름이 들어갑니다.
    private String orderName;

    // 장바구니 항목 ID 목록을 저장하는 리스트로, 여러 항목을 선택할 수 있습니다.
    private List<Integer> cartIdx;

    // 사용자 번호를 저장하는 필드로, 결제를 요청한 사용자를 식별할 수 있습니다.
    private Long uNo;

    // 결제 성공 시 리다이렉트될 URL을 저장하는 필드입니다.
    private String yourSuccessUrl;

    // 결제 실패 시 리다이렉트될 URL을 저장하는 필드입니다.
    private String yourFailureUrl;

    // 이 DTO 데이터를 Payment 엔티티로 변환하여 데이터베이스에 저장할 수 있도록 변환해주는 메서드입니다.
    public PaymentReqDTO toEntity() {
        // PaymentReqDTO 객체를 생성하는 빌더 패턴을 사용합니다.
        return PaymentReqDTO.builder()
                .PayType(PayType)  // 결제 수단 설정
                .price(price)  // 결제 금액 설정
                .orderName(orderName)  // 주문 이름 설정
                .cartIdx(cartIdx)  // 장바구니 항목 설정
                .uNo(uNo)  // 사용자 번호 설정
                .yourSuccessUrl(yourSuccessUrl)  // 결제 성공 시 리다이렉트될 URL 설정
                .yourFailureUrl(yourFailureUrl)  // 결제 실패 시 리다이렉트될 URL 설정
                .build();  // 최종적으로 PaymentReqDTO 객체를 생성하여 반환
    }
}
