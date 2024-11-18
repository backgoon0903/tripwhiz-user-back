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

    // 상품 목록 조회
    @GetMapping("/list")
    public ResponseEntity<PageResponseDTO<ProductListDTO>> list(@Validated PageRequestDTO requestDTO) {
        log.info("상품 목록을 조회합니다.");

        PageResponseDTO<ProductListDTO> response = productService.getList(requestDTO);
        log.info("상품 목록 응답: {}", response);

        return ResponseEntity.ok(response);
    }

    // 특정 상품 ID로 조회 (이미지 포함)
    @GetMapping("/read/{pno}")
    public ResponseEntity<ProductReadDTO> getProduct(@PathVariable Long pno) {
        log.info("ID로 상품을 조회합니다: {}", pno);

        Optional<ProductReadDTO> productObj = productService.getProductWithImage(pno);

        return productObj.map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("해당 ID의 상품을 찾을 수 없습니다: {}", pno);
                    return ResponseEntity.notFound().build();
                });
    }

    // 상위 카테고리(cno)로 상품 목록 조회
    @GetMapping("/list/category")
    public ResponseEntity<PageResponseDTO<ProductListDTO>> listByCategory(@RequestParam Long cno, @Validated PageRequestDTO requestDTO) {
        log.info("카테고리 ID(cno)로 상품 목록을 조회합니다: {}", cno);

        PageResponseDTO<ProductListDTO> response = productService.listByCategory(cno, requestDTO);
        log.info("카테고리별 상품 목록 응답: {}", response);

        return ResponseEntity.ok(response);
    }

    // 하위 카테고리(scno)로 상품 목록 조회
    @GetMapping("/list/subcategory")
    public ResponseEntity<PageResponseDTO<ProductListDTO>> listBySubCategory(@RequestParam Long scno, @Validated PageRequestDTO requestDTO) {
        log.info("하위 카테고리 ID(scno)로 상품 목록을 조회합니다: {}", scno);

        PageResponseDTO<ProductListDTO> response = productService.listBySubCategory(scno, requestDTO);
        log.info("하위 카테고리별 상품 목록 응답: {}", response);

        return ResponseEntity.ok(response);
    }

    // 테마 카테고리(tno)로 상품 목록 조회
    @GetMapping("/list/theme")
    public ResponseEntity<PageResponseDTO<ProductListDTO>> listByTheme(@RequestParam Long tno, @Validated PageRequestDTO requestDTO) {
        log.info("테마 카테고리 ID(tno)로 상품 목록을 조회합니다: {}", tno);

        PageResponseDTO<ProductListDTO> response = productService.listByTheme(tno, requestDTO);
        log.info("테마 카테고리별 상품 목록 응답: {}", response);

        return ResponseEntity.ok(response);
    }
}
