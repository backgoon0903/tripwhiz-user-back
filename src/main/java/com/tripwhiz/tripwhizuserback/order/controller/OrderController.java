package com.tripwhiz.tripwhizuserback.order.controller;

import com.tripwhiz.tripwhizuserback.common.dto.PageRequestDTO;
import com.tripwhiz.tripwhizuserback.common.dto.PageResponseDTO;
import com.tripwhiz.tripwhizuserback.order.dto.OrderListDTO;
import com.tripwhiz.tripwhizuserback.order.dto.OrderReadDTO;
import com.tripwhiz.tripwhizuserback.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ord")
@Log4j2
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // 주문 리스트 조회(zustand store에서 이메일 값 전달받아야 함)
    @GetMapping("/list")
    public PageResponseDTO<OrderListDTO> getOrderList(
            @RequestParam String email, PageRequestDTO pageRequestDTO) {
        return orderService.getOrderList(pageRequestDTO, email);
    }

    // 주문 취소
    @PutMapping("/cancel/{ono}")
    public ResponseEntity<String> cancelOrder(@PathVariable Long ono) {
        boolean result = orderService.cancelOrder(ono);
        if (result) {
            return ResponseEntity.ok("Order cancelled successfully.");
        } else {
            return ResponseEntity.badRequest().body("Failed to cancel order. Order may not exist or is already cancelled.");
        }
    }

    // 주문 상세 조회
    @GetMapping("/details/{ono}")
    public ResponseEntity<OrderReadDTO> getOrderDetails(@PathVariable Long ono) {
        OrderReadDTO orderDetails = orderService.getOrderDetails(ono);

        log.info(orderDetails);

        return ResponseEntity.ok(orderDetails);
    }

}
