package com.tripwhiz.tripwhizuserback.order.service;

import com.tripwhiz.tripwhizuserback.common.dto.PageRequestDTO;
import com.tripwhiz.tripwhizuserback.common.dto.PageResponseDTO;
import com.tripwhiz.tripwhizuserback.order.domain.Order;
import com.tripwhiz.tripwhizuserback.order.domain.OrderDetails;
import com.tripwhiz.tripwhizuserback.order.domain.OrderStatus;
import com.tripwhiz.tripwhizuserback.order.dto.OrderListDTO;
import com.tripwhiz.tripwhizuserback.order.dto.OrderProductDTO;
import com.tripwhiz.tripwhizuserback.order.dto.OrderReadDTO;
import com.tripwhiz.tripwhizuserback.order.repository.OrderDetailsRepository;
import com.tripwhiz.tripwhizuserback.order.repository.OrderRepository;
import com.tripwhiz.tripwhizuserback.product.dto.ProductDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final OrderDetailsRepository orderDetailsRepository;

    // 주문 리스트 조회
    public PageResponseDTO<OrderListDTO> getOrderList(PageRequestDTO pageRequestDTO, String email) {
        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() - 1, // 페이지는 0부터 시작
                pageRequestDTO.getSize()
        );

        Page<Order> result = orderRepository.findByMemberEmail(email, pageable);

        // OrderListDTO 생성
        List<OrderListDTO> dtoList = result.getContent().stream().map(order -> {
            // 주문별 OrderDetails 가져오기
            List<OrderDetails> orderDetailsList = orderDetailsRepository.findByOrderOno(order.getOno());

            // OrderDetails에서 totalAmount 계산
            int totalAmount = orderDetailsList.stream()
                    .mapToInt(OrderDetails::getAmount) // 각 OrderDetails의 amount 합산
                    .sum();

            return OrderListDTO.builder()
                    .ono(order.getOno())
                    .name(order.getMember().getName())
                    .totalAmount(totalAmount) // 계산된 totalAmount 추가
                    .totalPrice(order.getTotalPrice())
                    .createTime(order.getCreatetime())
                    .pickUpDate(order.getPickupdate())
                    .status(order.getStatus().name())
                    .build();
        }).collect(Collectors.toList());

        return PageResponseDTO.<OrderListDTO>withAll()
                .dtoList(dtoList)
                .pageRequestDTO(pageRequestDTO)
                .totalCount(result.getTotalElements())
                .build();
    }

    // 주문 취소 로직
    public boolean cancelOrder(Long ono) {
        // 주문 조회
        Optional<Order> optionalOrder = orderRepository.findById(ono);
        if (optionalOrder.isEmpty()) {
            log.warn("Order with ID {} not found", ono);
            return false; // 취소 실패
        }

        Order order = optionalOrder.get();

        // 상태를 CANCELLED로 변경
        if (order.getStatus() == OrderStatus.CANCELLED) {
            log.info("Order with ID {} is already cancelled.", ono);
            return false; // 이미 취소된 주문
        }

        order.setStatus(OrderStatus.CANCELLED); // 상태 변경
        orderRepository.save(order); // 저장
        log.info("Order with ID {} has been cancelled.", ono);
        return true; // 취소 성공
    }

    // 주문 번호로 주문 상세 조회
    public OrderReadDTO getOrderDetails(Long ono) {
        // 주문 번호로 주문 상세 엔티티 목록 조회
        List<OrderDetails> orderDetailsList = orderDetailsRepository.findByOrderOno(ono);

        // OrderDetails -> OrderProductDTO 변환
        List<OrderProductDTO> products = orderDetailsList.stream()
                .map(details -> OrderProductDTO.builder()
                        .product(ProductDTO.builder()
                                .pno(details.getProduct().getPno())
                                .pname(details.getProduct().getPname())
                                .price(details.getProduct().getPrice())
                                .build())
                        .amount(details.getAmount())
                        .build())
                .collect(Collectors.toList());

        // OrderReadDTO 반환
        return OrderReadDTO.builder()
                .ono(ono)
                .products(products)
                .build();
    }

}
