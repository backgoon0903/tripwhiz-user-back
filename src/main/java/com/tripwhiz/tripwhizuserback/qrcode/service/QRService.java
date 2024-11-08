package com.tripwhiz.tripwhizuserback.qrcode.service;

import com.google.zxing.BarcodeFormat;

import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
@Transactional
public class QRService {

    // QR 코드를 생성하는 메서드로, 주문 번호와 결제 금액을 포함하여 생성된 QR 코드 이미지의 Base64 URL을 반환
    public String generateQRCode(String orderId, int totalAmount) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter(); // QR 코드 생성을 담당하는 객체를 초기화

        // QR 코드에 포함할 텍스트 정보 작성
        // 주문 번호와 총 금액을 포함한 정보를 URL 인코딩하여 생성
        String qrText = "orderId=" + URLEncoder.encode(orderId, StandardCharsets.UTF_8) +
                "&totalAmount=" + URLEncoder.encode(String.valueOf(totalAmount), StandardCharsets.UTF_8);

        // QR 코드 생성을 위해 텍스트 정보와 QR 코드 형식, 크기(200x200)를 지정하여 BitMatrix로 반환
        BitMatrix bitMatrix = qrCodeWriter.encode(qrText, BarcodeFormat.QR_CODE, 200, 200);

        // BitMatrix 데이터를 이미지로 변환하여 바이트 배열로 저장하고, Base64 형식의 문자열로 반환
        try (ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream()) { // 이미지 데이터를 임시로 저장할 출력 스트림 생성
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream); // BitMatrix 데이터를 PNG 이미지로 변환하여 스트림에 저장
            byte[] pngData = pngOutputStream.toByteArray(); // 스트림에 저장된 데이터를 바이트 배열로 변환
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(pngData); // 바이트 배열을 Base64로 인코딩하여 URL 형식으로 반환
        }
    }
}
