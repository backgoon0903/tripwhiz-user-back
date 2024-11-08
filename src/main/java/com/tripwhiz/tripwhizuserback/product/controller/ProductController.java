package com.tripwhiz.tripwhizuserback.product.controller;

import com.tripwhiz.tripwhizuserback.common.dto.PageRequestDTO;
import com.tripwhiz.tripwhizuserback.common.dto.PageResponseDTO;
import com.tripwhiz.tripwhizuserback.product.dto.ProductListDTO;
import com.tripwhiz.tripwhizuserback.product.dto.ProductReadDTO;
import com.tripwhiz.tripwhizuserback.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/product")
@Log4j2
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/list/{dno}")
    public ResponseEntity<PageResponseDTO<ProductListDTO>> list(
            @Validated PageRequestDTO requestDTO,
            @PathVariable("dno") Long dno
    ){

        log.info("---------------------Product Controller list");
        log.info("======================");
        log.info(productService.list(requestDTO, dno));


        return ResponseEntity.ok(productService.list(requestDTO, dno));
    }

    @GetMapping("/read/{pno}")
    public ResponseEntity<ProductReadDTO> getProduct(@PathVariable Long pno) {
        Optional<ProductReadDTO> productObj = productService.getProductById(pno);

        log.info(pno);

        return productObj.isPresent() ? ResponseEntity.ok(productObj.get()) : ResponseEntity.notFound().build();

    }



}
