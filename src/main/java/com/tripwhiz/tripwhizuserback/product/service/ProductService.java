package com.tripwhiz.tripwhizuserback.product.service;

import com.tripwhiz.tripwhizuserback.common.dto.PageRequestDTO;
import com.tripwhiz.tripwhizuserback.common.dto.PageResponseDTO;
import com.tripwhiz.tripwhizuserback.product.domain.Product;
import com.tripwhiz.tripwhizuserback.product.dto.ProductListDTO;
import com.tripwhiz.tripwhizuserback.product.dto.ProductReadDTO;
import com.tripwhiz.tripwhizuserback.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // 상품 목록 조회
    public PageResponseDTO<ProductListDTO> getList(PageRequestDTO pageRequestDTO) {
        log.info("페이지 요청에 따라 상품 목록을 조회합니다: {}", pageRequestDTO);

        Pageable pageable = pageRequestDTO.getPageable();
        Page<ProductListDTO> productPage = productRepository.allProductList(pageable);

        if (productPage == null || productPage.isEmpty()) {
            log.warn("조회된 상품이 없습니다.");
            return new PageResponseDTO<>(List.of(), pageRequestDTO, 0);
        }

        return new PageResponseDTO<>(productPage.getContent(), pageRequestDTO, productPage.getTotalElements());
    }

    // 상품 ID로 단일 상품 조회
    public Optional<ProductReadDTO> getProductById(Long pno) {
        log.info("ID로 상품을 조회합니다: {}", pno);
        return productRepository.read(pno);
    }

    // 상위 카테고리(cno)로 상품 목록 조회
    public PageResponseDTO<ProductListDTO> listByCategory(Long cno, PageRequestDTO requestDTO) {
        log.info("카테고리 ID(cno)로 상품 목록을 조회합니다: {}", cno);

        Pageable pageable = requestDTO.getPageable();
        Page<ProductListDTO> products = productRepository.findByCategoryCno(cno, pageable);

        if (products.isEmpty()) {
            log.warn("해당 카테고리에 상품이 없습니다: {}", cno);
            return new PageResponseDTO<>(List.of(), requestDTO, 0);
        }

        return new PageResponseDTO<>(products.getContent(), requestDTO, products.getTotalElements());
    }

    // 하위 카테고리(scno)로 상품 목록 조회
    public PageResponseDTO<ProductListDTO> listBySubCategory(Long scno, PageRequestDTO requestDTO) {
        log.info("하위 카테고리 ID(scno)로 상품 목록을 조회합니다: {}", scno);

        Pageable pageable = requestDTO.getPageable();
        Page<ProductListDTO> products = productRepository.findBySubCategoryScno(scno, pageable);

        if (products.isEmpty()) {
            log.warn("해당 하위 카테고리에 상품이 없습니다: {}", scno);
            return new PageResponseDTO<>(List.of(), requestDTO, 0);
        }

        return new PageResponseDTO<>(products.getContent(), requestDTO, products.getTotalElements());
    }

    // 테마 카테고리(tno)로 상품 목록 조회
    public PageResponseDTO<ProductListDTO> listByTheme(Long tno, PageRequestDTO requestDTO) {
        log.info("테마 카테고리 ID(tno)로 상품 목록을 조회합니다: {}", tno);

        Pageable pageable = requestDTO.getPageable();
        Page<ProductListDTO> products = productRepository.findByThemeCategoryTno(tno, pageable);

        if (products.isEmpty()) {
            log.warn("해당 테마 카테고리에 상품이 없습니다: {}", tno);
            return new PageResponseDTO<>(List.of(), requestDTO, 0);
        }

        return new PageResponseDTO<>(products.getContent(), requestDTO, products.getTotalElements());
    }

    // 상품 정보와 이미지를 함께 조회
    public Optional<ProductReadDTO> getProductWithImage(Long pno) {
        log.info("ID로 상품 및 이미지를 조회합니다: {}", pno);

        return productRepository.read(pno).map(product -> {
            // 이미지 URL 처리
            if (product.getFileName() != null) {
                product.setFileName("/uploads/" + product.getFileName());
                log.info("이미지 URL 처리 완료: {}", product.getFileName());
            }
            return product;
        });
    }
}

// Admin API에서 전송된 상품 정보를 DB에 저장하는 메서드
//    public void saveProductFromAdmin(ProductListDTO productListDTO) {
//        log.info("Saving product from admin: {}", productListDTO);
//
//        // Category와 SubCategory를 찾음
//        Category category = categoryRepository.findById(productListDTO.getCategoryCno())
//                .orElseThrow(() -> new RuntimeException("Category not found for ID: " + productListDTO.getCategoryCno()));
//
//        SubCategory subCategory = subCategoryRepository.findById(productListDTO.getSubCategoryScno())
//                .orElseThrow(() -> new RuntimeException("SubCategory not found for ID: " + productListDTO.getSubCategoryScno()));
//
//        // ProductListDTO를 Product 엔티티로 변환 및 저장
//        Product product = productListDTO.toEntity(category, subCategory);
//        productRepository.save(product);
//
//        log.info("Product saved successfully: {}", product);
//    }

