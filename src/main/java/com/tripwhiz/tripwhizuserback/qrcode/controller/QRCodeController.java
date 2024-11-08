package com.tripwhiz.tripwhizuserback.qrcode.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

@RestController
@RequestMapping("/api")
public class QRCodeController {

    @PostMapping("/generate-qr")
    public Map<String, String> generateQRCode(@RequestBody Map<String, Object> payload) {
        String orderId = (String) payload.get("orderId");
        String qrContent = "주문번호:" + orderId + ",상품명:한글지원상품"; // 주문 번호와 상품명 포함

        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            Map<EncodeHintType, String> hintMap = new HashMap<>();
            hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8"); // 한글 인코딩 설정

            BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 200, 200, hintMap);
            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(qrImage, "png", baos);
            byte[] imageBytes = baos.toByteArray();

            String base64Image = "data:image/png;base64," + Base64.encodeBase64String(imageBytes);

            Map<String, String> response = new HashMap<>();
            response.put("qrCodeUrl", base64Image); // Base64 인코딩된 이미지 반환
            return response;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
