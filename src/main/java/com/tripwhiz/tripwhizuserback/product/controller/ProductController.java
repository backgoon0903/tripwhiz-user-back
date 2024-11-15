package com.tripwhiz.tripwhizuserback.product.controller;

import com.tripwhiz.tripwhizuserback.common.dto.PageRequestDTO;
import com.tripwhiz.tripwhizuserback.common.dto.PageResponseDTO;
import com.tripwhiz.tripwhizuserback.product.dto.ProductListDTO;
import com.tripwhiz.tripwhizuserback.product.dto.ProductReadDTO;
import com.tripwhiz.tripwhizuserback.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/product")
@Log4j2
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // 기본 상품 목록 조회 엔드포인트
    @GetMapping("/list")
    public ResponseEntity<PageResponseDTO<ProductListDTO>> list(@Validated PageRequestDTO requestDTO) {
        log.info("Fetching product list");

        PageResponseDTO<ProductListDTO> response = productService.list(requestDTO);
        log.info("Product list response: {}", response);

        return ResponseEntity.ok(response);
    }

    // 특정 상품 ID로 조회 엔드포인트
    @GetMapping("/read/{pno}")
    public ResponseEntity<ProductReadDTO> getProduct(@PathVariable Long pno) {
        log.info("Fetching product with ID: {}", pno);

        Optional<ProductReadDTO> productObj = productService.getProductById(pno);

        return productObj.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 상위 카테고리(cno)로 상품 목록 조회 엔드포인트
    @GetMapping("/list/category")
    public ResponseEntity<PageResponseDTO<ProductListDTO>> listByCategory(@RequestParam Long cno, @Validated PageRequestDTO requestDTO) {
        log.info("Fetching product list by category ID (cno): {}", cno);

        PageResponseDTO<ProductListDTO> response = productService.listByCategory(cno, requestDTO);
        log.info("Product list by category response: {}", response);

        return ResponseEntity.ok(response);
    }

    // 하위 카테고리(scno)로 상품 목록 조회 엔드포인트
    @GetMapping("/list/subcategory")
    public ResponseEntity<PageResponseDTO<ProductListDTO>> listBySubCategory(@RequestParam Long scno, @Validated PageRequestDTO requestDTO) {
        log.info("Fetching product list by sub-category ID (scno): {}", scno);

        PageResponseDTO<ProductListDTO> response = productService.listBySubCategory(scno, requestDTO);
        log.info("Product list by sub-category response: {}", response);

        return ResponseEntity.ok(response);
    }

    // 테마 카테고리로 상품 목록 조회 엔드포인트
    @GetMapping("/list/theme")
    public ResponseEntity<PageResponseDTO<ProductListDTO>> listByTheme(@RequestParam String themeCategory, @Validated PageRequestDTO requestDTO) {
        log.info("Fetching product list by theme category: {}", themeCategory);

        PageResponseDTO<ProductListDTO> response = productService.listByTheme(themeCategory, requestDTO);
        log.info("Product list by theme response: {}", response);

        return ResponseEntity.ok(response);
    }

    // Admin API에서 전송하는 상품 정보를 받아 저장하는 엔드포인트
    @PostMapping("/add")
    public ResponseEntity<String> addProduct(@RequestBody ProductListDTO productListDTO) {
        log.info("Receiving new product data from Admin API");

        // ProductListDTO를 Product 엔티티로 변환하여 DB에 저장
        productService.saveProductFromAdmin(productListDTO);

        return ResponseEntity.ok("Product saved successfully from Admin API");
    }
}
