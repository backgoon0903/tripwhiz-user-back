package com.tripwhiz.tripwhizuserback.product.service;

import com.tripwhiz.tripwhizuserback.category.domain.Category;
import com.tripwhiz.tripwhizuserback.category.domain.SubCategory;
import com.tripwhiz.tripwhizuserback.category.repository.CategoryRepository;
import com.tripwhiz.tripwhizuserback.category.repository.SubCategoryRepository;
import com.tripwhiz.tripwhizuserback.common.dto.PageRequestDTO;
import com.tripwhiz.tripwhizuserback.common.dto.PageResponseDTO;
import com.tripwhiz.tripwhizuserback.product.domain.Product;
import com.tripwhiz.tripwhizuserback.product.domain.ThemeCategory;
import com.tripwhiz.tripwhizuserback.product.dto.ProductListDTO;
import com.tripwhiz.tripwhizuserback.product.dto.ProductReadDTO;
import com.tripwhiz.tripwhizuserback.product.repository.ProductRepository;
import com.tripwhiz.tripwhizuserback.util.CustomFileUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final CustomFileUtil customFileUtil;

    // 상품 목록 조회
    public PageResponseDTO<ProductListDTO> getList(PageRequestDTO pageRequestDTO) {
        log.info("Fetching product list with pagination: {}", pageRequestDTO);

        // PageRequestDTO를 Pageable로 변환
        Pageable pageable = pageRequestDTO.getPageable();

        // ProductRepository에서 list(Pageable) 메서드 호출
        Page<ProductListDTO> productPage = productRepository.allProductList(pageable);

        // Null 체크 추가
        if (productPage == null || productPage.isEmpty()) {
            log.warn("No products found");
            return new PageResponseDTO<>(List.of(), pageRequestDTO, 0);
        }
        // PageResponseDTO로 변환하여 반환
        return new PageResponseDTO<>(productPage.getContent(), pageRequestDTO, productPage.getTotalElements());
    }

//     상품 ID로 단일 상품 조회
    public Optional<ProductReadDTO> getProductById(Long pno) {
        log.info("Fetching product by ID: {}", pno);
        return productRepository.read(pno);
    }

    // 상위 카테고리(cno)로 상품 목록 조회
    public PageResponseDTO<ProductListDTO> listByCategory(Long cno, PageRequestDTO requestDTO) {
        log.info("Fetching product list by category ID (cno): {}", cno);

        Pageable pageable = requestDTO.getPageable();
        Page<ProductListDTO> products = productRepository.findByCategoryCno(cno, pageable);

        if (products.isEmpty()) {
            log.warn("No products found for category ID: {}", cno);
            return new PageResponseDTO<>(List.of(), requestDTO, 0);
        }

        return new PageResponseDTO<>(products.getContent(), requestDTO, products.getTotalElements());
    }

    // 하위 카테고리(scno)로 상품 목록 조회
    public PageResponseDTO<ProductListDTO> listBySubCategory(Long scno, PageRequestDTO requestDTO) {
        log.info("Fetching product list by sub-category ID (scno): {}", scno);

        Pageable pageable = requestDTO.getPageable();
        Page<ProductListDTO> products = productRepository.findBySubCategoryScno(scno, pageable);

        if (products.isEmpty()) {
            log.warn("No products found for sub-category ID: {}", scno);
            return new PageResponseDTO<>(List.of(), requestDTO, 0);
        }

        return new PageResponseDTO<>(products.getContent(), requestDTO, products.getTotalElements());
    }

    // 테마 카테고리 tno로 상품 목록 조회
    public PageResponseDTO<ProductListDTO> listByTheme(Long tno, PageRequestDTO requestDTO) {
        log.info("Fetching product list by theme category tno: {}", tno);

        Pageable pageable = requestDTO.getPageable();

        // tno를 이용해 상품 목록 조회
        Page<ProductListDTO> products = productRepository.findByThemeCategoryTno(tno, pageable);

        if (products.isEmpty()) {
            log.warn("No products found for theme category tno: {}", tno);
            return new PageResponseDTO<>(List.of(), requestDTO, 0);
        }

        return new PageResponseDTO<>(products.getContent(), requestDTO, products.getTotalElements());
    }

    // 상품 정보와 이미지를 함께 조회
    public Optional<ProductReadDTO> getProductWithImage(Long pno) {
        log.info("Fetching product with image by ID: {}", pno);
        Optional<ProductReadDTO> productOptional = productRepository.read(pno);

        return productOptional.map(product -> {
            if (product.getFileUrl() != null) {
                customFileUtil.getFile(product.getFileUrl());
                log.info("Image retrieved for product: {}", product.getFileUrl());
            }
            return product;
        });
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
}
