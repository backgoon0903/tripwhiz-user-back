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
    public ResponseEntity<PageResponseDTO<ProductListDTO>> list(
            @RequestParam(required = false) Long tno,
            @RequestParam(required = false) Long cno,
            @RequestParam(required = false) Long scno,
            @Validated PageRequestDTO requestDTO) {
        log.info("상품 목록을 조회합니다. tno: {}, cno: {}, scno: {}", tno, cno, scno);

        PageResponseDTO<ProductListDTO> response;

        if (tno != null) {
            // 테마로 필터링
            Optional<Long> optionalTno = Optional.ofNullable(tno);
            response = productService.listByTheme(optionalTno, requestDTO);
        } else if (cno != null && scno != null) {
            // 상위 카테고리와 하위 카테고리로 필터링
            response = productService.listByCategoryAndSubCategory(cno, scno, requestDTO);
        } else if (cno != null) {
            // 상위 카테고리로 필터링
            response = productService.listByCategory(cno, requestDTO);
        } else {
            // 전체 목록 조회 (기존에 이런 메소드가 없다면, 여기에 추가)
            response = productService.getList(requestDTO);
        }
        log.info("상품 목록 응답: {}", response);
        return ResponseEntity.ok(response);
    }


    // 특정 상품 ID로 조회 (이미지 포함)
    @GetMapping("/read/{pno}")
    public ResponseEntity<ProductReadDTO> getProduct(@PathVariable Long pno) {
        log.info("ID로 상품을 조회합니다: {}", pno);

        Optional<ProductReadDTO> productObj = productService.getProductById(pno);

        return productObj.map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("해당 ID의 상품을 찾을 수 없습니다: {}", pno);
                    return ResponseEntity.notFound().build();
                });
    }

}
