package com.tripwhiz.tripwhizuserback.payment.repository;

import com.tripwhiz.tripwhizuserback.payment.domain.PaymentEntity;
import com.tripwhiz.tripwhizuserback.payment.domain.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<PaymentEntity,Long> {

    // 기본적인 CRUD 기능은 JpaRepository 에서 제공하기때문에 작성 안해도됨

    // 결제 상태로 결제 목록을 조회하는 메서드
    List<PaymentEntity> findByStatus(String status);

    // 사용자 번호로 결제 목록 조회하는 메서드
    List<PaymentEntity> findByUNo(int uNo);

    // 특정 주문 번호로 결제를 조화하는 메서드
    Optional<PaymentEntity> findByONo(int oNo);

    // 결제가 성공한 목록조회(결제 상태와 성공 여부를 함꼐 확인)
    List<PaymentEntity> findByStatusAndPaySuccess(PaymentStatus status, boolean paySuccess);

    // 환불 여부에 따른 결제 목록조회
    List<PaymentEntity> findByIsRefunded(boolean isRefunded);

    // 취소된 결재 목록 조회(취소일자가 null이 아닌 경우)
    List<PaymentEntity> findByCanceledAtNotNull();


}
