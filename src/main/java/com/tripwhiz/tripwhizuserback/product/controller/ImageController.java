package com.tripwhiz.tripwhizuserback.product.controller;

import com.tripwhiz.tripwhizuserback.product.service.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file, @RequestParam Long pno) {
        try {
            // 업로드된 파일을 저장하고, 접근 가능한 URL을 반환
            String fileUrl = imageService.uploadImage(file, pno);
            return ResponseEntity.ok("File uploaded successfully: " + fileUrl);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload image: " + e.getMessage());
        }
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveImages(@RequestParam Long pno) {
        try {
            // 지정된 디렉토리에서 이미지 파일을 읽어와 특정 제품에 저장
            imageService.saveImagesFromDirectory(pno);
            return ResponseEntity.ok("Images saved successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to save images: " + e.getMessage());
        }
    }
}
