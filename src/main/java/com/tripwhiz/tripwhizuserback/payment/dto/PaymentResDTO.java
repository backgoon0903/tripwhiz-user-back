package com.tripwhiz.tripwhizuserback.payment.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor // 모든 필드를 포함하는 생성자를 자동으로 생성해줍니다.
@NoArgsConstructor  // 기본 생성자를 자동으로 생성해줍니다.
@Builder            // 빌더 패턴을 사용할 수 있도록 지원하여, 객체 생성 시 편리하게 설정할 수 있습니다.
@Getter             // 모든 필드에 대한 getter 메서드를 자동으로 생성합니다.
@Setter             // 모든 필드에 대한 setter 메서드를 자동으로 생성합니다.
@ToString           // 객체의 모든 필드 정보를 포함하는 toString 메서드를 자동으로 생성합니다.
public class PaymentResDTO {

    // 결제 방식이 카드인지, 현금인지 등 구분하기 위해 필요합니다. 결제 방식을 서버에서 관리하고 클라이언트에서 보여줄 때 유용합니다.
    private String payType;

    // 결제 요청 금액을 저장하여 결제 금액과 요청 금액이 일치하는지 확인할 때 사용됩니다.
    private Integer amount;

    // 주문 이름이나 상품명을 저장하여 사용자가 결제할 항목이 무엇인지 쉽게 알 수 있도록 합니다.
    private String orderName;

    // 각 주문을 구분하기 위한 고유한 ID로, 결제나 주문 기록을 조회할 때 사용됩니다.
    private String orderId;

    // 결제가 이루어진 시간을 저장하여 결제 기록을 추적하고, 정산 등의 작업에 활용할 수 있습니다.
    private LocalDateTime createdDate;

    // 결제가 성공했는지 실패했는지 여부를 나타내며, 결제 상태를 쉽게 확인하고 처리 결과를 관리할 때 사용됩니다.
    private boolean paySuccess;

    // 결제 식별을 위한 고유한 키로, 외부 결제 시스템과의 통신에서 특정 결제 건을 조회하거나 조작할 때 사용됩니다.
    private String paymentKey;

    // 결제 세션을 식별하기 위해 사용되며, 사용자의 세션에 따라 특정 결제 작업을 분리하고 관리할 때 유용합니다.
    private String sessionId;

    // 결제가 성공했을 때 사용자를 리다이렉트할 URL로, 결제 완료 화면 등으로 연결하는데 사용됩니다.
    private String successUrl;

    // 결제 실패 시 사용자를 리다이렉트할 URL로, 결제가 실패했을 때 오류 페이지 등으로 연결하는데 사용됩니다.
    private String failUrl;

    // 결제가 실패했을 때 그 이유를 저장하여, 실패 원인을 사용자에게 보여주거나 로그에 기록할 때 사용됩니다.
    private String failReason;

    // 결제가 취소되었는지 여부를 나타내며, 결제 취소 여부를 관리하고 취소 처리 결과를 기록하는데 사용됩니다.
    private boolean cancel;

    // 결제 취소가 발생했을 때 그 이유를 저장하여, 취소 원인을 사용자에게 보여주거나 내부 기록으로 남길 때 사용됩니다.
    private String cancelReason;

}
