package com.tripwhiz.tripwhizuserback.payment.service;

import com.tripwhiz.tripwhizuserback.payment.domain.PaymentEntity;
import com.tripwhiz.tripwhizuserback.payment.domain.PaymentStatus;
import com.tripwhiz.tripwhizuserback.payment.dto.PaymentReqDTO;
import com.tripwhiz.tripwhizuserback.payment.dto.PaymentResDTO;
import com.tripwhiz.tripwhizuserback.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//아직 entity 를 안만들어서 오류발생 11.6. 오전중으로 entity 만들고 set,get 처리 해줘야함!-SO

// @Log4j2: 로깅을 위한 어노테이션입니다. log.info()와 같은 메서드를 사용하여 로그를 기록할 수 있게 해줍니다.
@Log4j2
// @Service: 이 클래스가 Spring의 서비스 컴포넌트로 사용됨을 나타내며, 비즈니스 로직을 처리하는 클래스임을 명시합니다.
@Service
// @Transactional: 이 클래스의 메서드들이 실행되는 동안 트랜잭션이 유지되도록 하여 데이터의 일관성을 보장합니다.
@Transactional
// @RequiredArgsConstructor: final로 선언된 필드를 인자로 하는 생성자를 자동으로 생성해줍니다. 여기서는 paymentRepository 필드를 초기화합니다.
@RequiredArgsConstructor
public class PaymentService {

    // PaymentRepository 객체를 주입받아, 데이터베이스와의 상호작용을 처리합니다.
    private final PaymentRepository paymentRepository;

    // 새로운 결제를 생성하고 데이터베이스에 저장하는 메서드입니다.
    public PaymentResDTO createPayment(PaymentReqDTO paymentReqDTO) {
        // PaymentEntity 객체를 생성하여 결제 데이터를 저장할 준비를 합니다.
        PaymentEntity paymentEntity = new PaymentEntity();
        // 결제 방식 설정: 결제 요청 DTO에 있는 결제 타입 정보를 엔티티에 설정합니다.
        paymentEntity.setPayType(paymentReqDTO.getPayType());
        // 결제 금액 설정: 요청에서 전달된 금액을 엔티티에 설정합니다.
        paymentEntity.setAmount(paymentReqDTO.getPrice());
        // 주문 이름 설정: 사용자가 지정한 주문 이름을 설정하여 주문 정보를 엔티티에 저장합니다.
        paymentEntity.setOrderName(paymentReqDTO.getOrderName());
        // 사용자 번호 설정: 결제 요청을 한 사용자 번호를 엔티티에 설정합니다.
        paymentEntity.setUNo(paymentReqDTO.getUNo());
        // 결제 성공 시 리다이렉트될 URL 설정: 성공 URL 정보를 엔티티에 설정합니다.
        paymentEntity.setSuccessUrl(paymentReqDTO.getYourSuccessUrl());
        // 결제 실패 시 리다이렉트될 URL 설정: 실패 URL 정보를 엔티티에 설정합니다.
        paymentEntity.setFailUrl(paymentReqDTO.getYourFailureUrl());
        // 결제 생성 시간 설정: 결제 요청 시의 현재 시간을 생성 시간으로 설정합니다.
        paymentEntity.setCreatedDate(LocalDateTime.now());
        // 결제 성공 여부 초기화: 처음에는 결제 상태를 실패로 설정하고 이후 성공 시 업데이트될 수 있게 설정합니다.
        paymentEntity.setPaySuccess(false);

        // paymentRepository를 사용하여 설정된 PaymentEntity 객체를 데이터베이스에 저장합니다.
        PaymentEntity savedPayment = paymentRepository.save(paymentEntity);

        // 저장된 PaymentEntity 객체를 PaymentResDTO로 변환하여 반환합니다.
        return convertToDTO(savedPayment);
    }

    // 특정 결제 상태에 해당하는 결제 목록을 조회하는 메서드입니다.
    public List<PaymentResDTO> getPaymentsByStatus(String status) {
        // PaymentRepository의 findByStatus 메서드를 호출하여 특정 상태의 결제 목록을 가져오고,
        // 각 결제 정보를 PaymentResDTO로 변환하여 리스트로 반환합니다.
        return paymentRepository.findByStatus(status)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 특정 사용자 번호에 해당하는 결제 목록을 조회하는 메서드입니다.
    public List<PaymentResDTO> getPaymentsByUser(int uNo) {
        // 사용자의 결제 목록을 가져와서 각 결제를 DTO로 변환하고 리스트로 반환합니다.
        return paymentRepository.findByUNo(uNo)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 특정 주문 번호에 해당하는 결제 정보를 조회하는 메서드입니다.
    public Optional<PaymentResDTO> getPaymentByOrderNo(int oNo) {
        // 주문 번호로 조회한 결제 정보를 Optional로 반환하며, 결제 정보가 없을 경우 빈 Optional을 반환합니다.
        return paymentRepository.findByONo(oNo).map(this::convertToDTO);
    }

    // 결제 상태와 성공 여부가 일치하는 결제 목록을 조회하는 메서드입니다.
    public List<PaymentResDTO> getSuccessfulPayments(PaymentStatus status) {
        // 상태와 성공 여부에 맞는 결제 목록을 조회하고, DTO 형식으로 변환하여 반환합니다.
        return paymentRepository.findByStatusAndPaySuccess(status, true)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 환불 여부에 따른 결제 목록을 조회하는 메서드입니다.
    public List<PaymentResDTO> getRefundedPayments(boolean isRefunded) {
        // 환불 여부가 일치하는 결제 목록을 조회하고, DTO로 변환하여 반환합니다.
        return paymentRepository.findByIsRefunded(isRefunded)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 취소된 결제 목록을 조회하는 메서드입니다.
    public List<PaymentResDTO> getCanceledPayments() {
        // 취소일자가 null이 아닌 결제 목록을 조회하여 DTO로 변환 후 반환합니다.
        return paymentRepository.findByCanceledAtNotNull()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // PaymentEntity 객체를 PaymentResDTO로 변환하여 반환하는 헬퍼 메서드입니다.
    private PaymentResDTO convertToDTO(PaymentEntity entity) {
        // PaymentEntity 객체의 정보를 기반으로 PaymentResDTO 객체를 생성하여 반환합니다.
        return PaymentResDTO.builder()
                .payType(entity.getPayType()) // 결제 방식 설정
                .amount(entity.getAmount()) // 결제 금액 설정
                .orderName(entity.getOrderName()) // 주문 이름 설정
                .orderId(entity.getOrderId()) // 주문 ID 설정
                .createdDate(entity.getCreatedDate()) // 생성 시간 설정
                .paySuccess(entity.isPaySuccess()) // 결제 성공 여부 설정
                .paymentKey(entity.getPaymentKey()) // 결제 고유 키 설정
                .sessionId(entity.getSessionId()) // 세션 ID 설정
                .successUrl(entity.getSuccessUrl()) // 성공 URL 설정
                .failUrl(entity.getFailUrl()) // 실패 URL 설정
                .failReason(entity.getFailReason()) // 실패 이유 설정
                .cancel(entity.isCancel()) // 결제 취소 여부 설정
                .cancelReason(entity.getCancelReason()) // 결제 취소 이유 설정
                .build();
    }
}
