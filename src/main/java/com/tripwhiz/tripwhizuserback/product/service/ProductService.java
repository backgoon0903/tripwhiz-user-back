package com.tripwhiz.tripwhizuserback.product.service;

import com.tripwhiz.tripwhizuserback.category.domain.Category;
import com.tripwhiz.tripwhizuserback.category.domain.SubCategory;
import com.tripwhiz.tripwhizuserback.category.repository.CategoryRepository;
import com.tripwhiz.tripwhizuserback.category.repository.SubCategoryRepository;
import com.tripwhiz.tripwhizuserback.common.dto.PageRequestDTO;
import com.tripwhiz.tripwhizuserback.common.dto.PageResponseDTO;
import com.tripwhiz.tripwhizuserback.product.domain.Product;
import com.tripwhiz.tripwhizuserback.product.dto.ProductListDTO;
import com.tripwhiz.tripwhizuserback.product.dto.ProductReadDTO;
import com.tripwhiz.tripwhizuserback.product.repository.ProductRepository;
import com.tripwhiz.tripwhizuserback.util.CustomFileUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

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

    // 기본 상품 목록 조회
    public PageResponseDTO<ProductListDTO> list(PageRequestDTO pageRequestDTO) {
        log.info("Fetching product list with pagination");
        return productRepository.listByCno(pageRequestDTO);
    }

    // 상품 ID로 단일 상품 조회
    public Optional<ProductReadDTO> getProductById(Long pno) {
        return productRepository.read(pno);
    }

    // 상위 카테고리(cno)로 상품 목록 조회
    public PageResponseDTO<ProductListDTO> listByCategory(Long cno, PageRequestDTO pageRequestDTO) {
        log.info("Fetching product list by category ID (cno): {}", cno);
        return productRepository.listByCategory(cno, pageRequestDTO);
    }

    // 하위 카테고리(scno)로 상품 목록 조회
    public PageResponseDTO<ProductListDTO> listBySubCategory(Long scno, PageRequestDTO pageRequestDTO) {
        log.info("Fetching product list by sub-category ID (scno): {}", scno);
        return productRepository.listBySubCategory(scno, pageRequestDTO);
    }

    // 테마 카테고리로 상품 목록 조회
    public PageResponseDTO<ProductListDTO> listByTheme(String themeCategory, PageRequestDTO pageRequestDTO) {
        log.info("Fetching product list by theme category: {}", themeCategory);
        return productRepository.listByTheme(themeCategory, pageRequestDTO);
    }

    // 상품 정보와 이미지를 함께 조회
    public Optional<ProductReadDTO> getProductWithImage(Long pno) {
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
    public void saveProductFromAdmin(ProductListDTO productListDTO) {
        // Category와 SubCategory를 찾음
        Category category = categoryRepository.findById(productListDTO.getCategoryCno())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        SubCategory subCategory = subCategoryRepository.findById(productListDTO.getSubCategoryScno())
                .orElseThrow(() -> new RuntimeException("SubCategory not found"));

        // ProductListDTO를 Product 엔티티로 변환
        Product product = productListDTO.toEntity(category, subCategory);

        // DB에 저장
        productRepository.save(product);
    }
}
