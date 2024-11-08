package com.tripwhiz.tripwhizuserback.qrcode.controller;

import com.tripwhiz.tripwhizuserback.qrcode.service.QRService;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class QRCodeController {

    private final QRService qrService; // QR 코드 생성을 담당하는 서비스 객체

    // 결제 완료 시 QR 코드를 생성하는 엔드포인트
    @PostMapping("/complete")
    public Map<String, String> completeOrder(@RequestParam String ono, @RequestParam int totalAmount) throws Exception {
        // QR 코드 생성 요청 및 결과를 반환할 데이터 맵 초기화
        String qrCodeBase64 = qrService.generateQRCode(ono, totalAmount);

        // 응답 메시지 및 QR 코드 Base64 문자열 포함
        Map<String, String> response = new HashMap<>();
        response.put("message", "주문이 완료되었습니다.");
        response.put("qrCode", qrCodeBase64);

        return response; // 생성된 QR 코드와 메시지를 JSON 형식으로 반환
    }
}
