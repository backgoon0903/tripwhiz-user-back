package com.tripwhiz.tripwhizuserback.product.controller;

import com.tripwhiz.tripwhizuserback.product.dto.ProductReadDTO;
import com.tripwhiz.tripwhizuserback.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Log4j2
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{pno}")
    public ResponseEntity<ProductReadDTO> getProduct(@PathVariable Long pno) {
        Optional<ProductReadDTO> productObj = productService.getProductById(pno);

        return productObj.isPresent() ? ResponseEntity.ok(productObj.get()) : ResponseEntity.notFound().build();

    }

}
