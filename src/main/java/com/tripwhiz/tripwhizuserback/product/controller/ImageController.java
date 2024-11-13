package com.tripwhiz.tripwhizuserback.product.controller;

import com.tripwhiz.tripwhizuserback.product.service.ImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final ImageService imageService;

    // 생성자 주입 방식 사용
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    // 디렉토리의 이미지를 데이터베이스에 저장하는 엔드포인트
    @GetMapping("/save")
    public ResponseEntity<String> saveImages() {
        try {
            String path = "C:\\zzz\\upload";
            imageService.saveImagesWithUrl(path);  // 받은 경로로 이미지 저장
            return ResponseEntity.ok("Images saved successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to save images: " + e.getMessage());
        }
    }
}
