package com.tripwhiz.tripwhizuserback.product.service;

import com.tripwhiz.tripwhizuserback.common.dto.PageRequestDTO;
import com.tripwhiz.tripwhizuserback.product.dto.ProductListDTO;
import com.tripwhiz.tripwhizuserback.product.dto.ProductReadDTO;
import com.tripwhiz.tripwhizuserback.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // 상품 목록 조회
    public Page<ProductListDTO> getList(PageRequestDTO pageRequestDTO) {

        log.info("페이지 요청에 따라 상품 목록을 조회합니다: {}", pageRequestDTO);

        return productRepository.list(pageRequestDTO);

    }

    // 상품 ID로 단일 상품 조회
    public Optional<ProductReadDTO> getProductById(Long pno) {
        log.info("ID로 상품을 조회합니다: {}", pno);
        return productRepository.read(pno);
    }

    // 상위 카테고리(cno)로 상품 목록 조회
    public Page<ProductListDTO> listByCategory(Long cno, PageRequestDTO pageRequestDTO) {
        log.info("카테고리 ID(cno)로 상품 목록을 조회합니다: {}", cno);

        return productRepository.findByCategory(cno, pageRequestDTO);

    }

    // cno와 하위 카테고리(scno)로 상품 목록 조회
    public Page<ProductListDTO> listByCategoryAndSubCategory(Long cno, Long scno, PageRequestDTO pageRequestDTO) {
        log.info("카테고리 ID(cno)와 하위 카테고리 ID(scno)로 상품 목록을 조회합니다: {}, {}", cno, scno);

        return productRepository.findByCategoryAndSubCategory(cno, scno, pageRequestDTO);

    }

    // 테마 카테고리(tno)로 상품 목록 조회
    public Page<ProductListDTO> listByTheme(Optional<Long> tno, PageRequestDTO pageRequestDTO) {
        log.info("테마 카테고리 ID(tno)로 상품 목록을 조회합니다: {}", tno);

        return productRepository.findByThemeCategory(tno, pageRequestDTO);

    }

//    private ProductListDTO entityToDto(Product product) {
//        return ProductListDTO.builder()
//                .pno(product.getPno())
//                .pname(product.getPname())
//                .price(product.getPrice())
//                .categoryCno(product.getCategory() != null ? product.getCategory().getCno() : null)
//                .subCategoryScno(product.getSubCategory() != null ? product.getSubCategory().getScno() : null)
//                .build();
//    }

    // 상품 정보와 이미지를 함께 조회
//    public Optional<ProductReadDTO> getProductWithImage(Long pno) {
//        log.info("ID로 상품 및 이미지를 조회합니다: {}", pno);
//
//        return productRepository.read(pno).map(product -> {
//            // 이미지 URL 처리
//            if (product.getFileName() != null) {
//                product.setFileName("/uploads/" + product.getFileName());
//                log.info("이미지 URL 처리 완료: {}", product.getFileName());
//            }
//            return product;
//        });
//    }


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
}
