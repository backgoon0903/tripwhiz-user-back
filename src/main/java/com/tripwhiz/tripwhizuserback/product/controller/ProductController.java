package com.tripwhiz.tripwhizuserback.product.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tripwhiz.tripwhizuserback.common.dto.PageRequestDTO;
import com.tripwhiz.tripwhizuserback.common.dto.PageResponseDTO;
import com.tripwhiz.tripwhizuserback.product.domain.Product;
import com.tripwhiz.tripwhizuserback.product.domain.ThemeCategory;
import com.tripwhiz.tripwhizuserback.product.dto.ProductListDTO;
import com.tripwhiz.tripwhizuserback.product.dto.ProductReadDTO;
import com.tripwhiz.tripwhizuserback.product.repository.ThemeCategoryRepository;
import com.tripwhiz.tripwhizuserback.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/product")
@Log4j2
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/add")
    public ResponseEntity<Long> createProduct(
            @RequestPart("productListDTO") String productListDTOJson,
            @RequestPart(value = "imageFiles", required = false) List<MultipartFile> imageFiles
    ) throws JsonProcessingException, IOException {
        log.info("===========================");
        log.info("Received productListDTO JSON: {}", productListDTOJson);
        log.info("Received Image Files: {}", imageFiles);

        // JSON -> DTO 변환
        ObjectMapper objectMapper = new ObjectMapper();
        ProductListDTO productListDTO;
        try {
            productListDTO = objectMapper.readValue(productListDTOJson, ProductListDTO.class);
        } catch (JsonProcessingException e) {
            log.error("JSON 파싱 오류: {}", e.getMessage());
            throw e;
        }

        log.info("Parsed ProductListDTO: {}", productListDTO);

        if (imageFiles != null) {
            log.info("Received {} image files", imageFiles.size());
        } else {
            log.warn("No image files were provided.");
        }

        // 상품 생성
        Long productId = productService.createProduct(productListDTO, imageFiles);

        return ResponseEntity.ok(productId);
    }


}

//    // 상품 생성
//    @PostMapping("/add")
//    public ResponseEntity<Long> createProduct(
//            @RequestPart("productListDTO") String productListDTOJson,
//            @RequestPart(value = "imageFiles", required = false) List<MultipartFile> imageFiles,
//            @RequestPart("themeCategoryId") Long themeCategoryId
//            ) throws JsonProcessingException, IOException {
//
//        log.info("===========================");
//        log.info(productListDTOJson);
//        log.info(themeCategoryId);
//        log.info(imageFiles);
//
//
//
//        // JSON 문자열을 객체로 변환
//        ObjectMapper objectMapper = new ObjectMapper();
//        ProductListDTO productListDTO = objectMapper.readValue(productListDTOJson, ProductListDTO.class);
//
//        log.info("Received Product: {}", productListDTO);
//
//        if (imageFiles != null) {
//            log.info("Received {} image files", imageFiles.size());
//        }
//
//        // 서비스 호출
//        Long productId = productService.createProduct(productListDTO, imageFiles);
//
//        // 생성된 상품 ID 반환
//        return ResponseEntity.ok(productId);
//    }
//
//    // 상품 수정
//    @PutMapping("/update/{pno}")
//    public ResponseEntity<Long> updateProduct(
//            @PathVariable Long pno,
//            @RequestPart("productListDTO") String productListDTOJson,
//            @RequestPart(value = "imageFiles", required = false) List<MultipartFile> imageFiles) throws JsonProcessingException, IOException {
//
//        // JSON 문자열을 객체로 변환
//        ObjectMapper objectMapper = new ObjectMapper();
//        ProductListDTO productListDTO = objectMapper.readValue(productListDTOJson, ProductListDTO.class);
//
//        log.info("Received product update request for PNO {}: {}", pno, productListDTO);
//
//        if (imageFiles != null) {
//            log.info("Received {} image files", imageFiles.size());
//        }
//
//        // 서비스 호출
//        Long updatedProductPno = productService.updateProduct(pno, productListDTO, imageFiles);
//
//        // 수정된 상품 ID 반환
//        return ResponseEntity.ok(updatedProductPno);
//    }
//
//    // 상품 삭제
//    @PutMapping("/delete/{pno}")
//    public ResponseEntity<Void> deleteProduct(@PathVariable Long pno) {
//        log.info("Received product deletion request for PNO {}", pno);
//        productService.deleteProduct(pno);
//        return ResponseEntity.ok().build();
//    }
//
//}
