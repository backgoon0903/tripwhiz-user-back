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
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;

    // 상품 목록 조회
    public PageResponseDTO<ProductListDTO> getList(PageRequestDTO pageRequestDTO) {
        log.info("페이지 요청에 따라 상품 목록을 조회합니다: {}", pageRequestDTO);

        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by(Sort.Direction.DESC, "pno") // pno를 기준으로 내림차순 정렬
        );

        Page<Product> productPage = productRepository.findAll(pageable);

        if (productPage.isEmpty()) {
            log.warn("조회된 상품이 없습니다.");
            return new PageResponseDTO<>(List.of(), pageRequestDTO, 0);
        }

        List<ProductListDTO> dtoList = productPage.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());

        return new PageResponseDTO<>(dtoList, pageRequestDTO, productPage.getTotalElements());
    }

    // 상품 ID로 단일 상품 조회
    public Optional<ProductReadDTO> getProductById(Long pno) {
        log.info("ID로 상품을 조회합니다: {}", pno);
        return productRepository.read(pno);
    }

    // 상위 카테고리(cno)로 상품 목록 조회
    public PageResponseDTO<ProductListDTO> listByCategory(Long cno, PageRequestDTO requestDTO) {
        log.info("카테고리 ID(cno)로 상품 목록을 조회합니다: {}", cno);

        Pageable pageable = PageRequest.of(
                requestDTO.getPage() - 1,
                requestDTO.getSize(),
                Sort.by(Sort.Direction.DESC, "pno") // pno를 기준으로 내림차순 정렬
        );

        Page<ProductListDTO> products = productRepository.findByCategory(cno, pageable);

        if (products.isEmpty()) {
            log.warn("해당 카테고리에 상품이 없습니다: {}", cno);
            return new PageResponseDTO<>(List.of(), requestDTO, 0);
        }

        return new PageResponseDTO<>(products.getContent(), requestDTO, products.getTotalElements());
    }


    // cno와 하위 카테고리(scno)로 상품 목록 조회
    public PageResponseDTO<ProductListDTO> listByCategoryAndSubCategory(Long cno, Long scno, PageRequestDTO requestDTO) {
        log.info("카테고리 ID(cno)와 하위 카테고리 ID(scno)로 상품 목록을 조회합니다: {}, {}", cno, scno);

        Pageable pageable = PageRequest.of(
                requestDTO.getPage() - 1,
                requestDTO.getSize(),
                Sort.by(Sort.Direction.DESC, "pno") // pno를 기준으로 내림차순 정렬
        );

        Page<ProductListDTO> products = productRepository.findByCategoryAndSubCategory(cno, scno, pageable);

        if (products.isEmpty()) {
            log.warn("해당 카테고리와 하위 카테고리에 상품이 없습니다: {}, {}", cno, scno);
            return new PageResponseDTO<>(List.of(), requestDTO, 0);
        }

        return new PageResponseDTO<>(products.getContent(), requestDTO, products.getTotalElements());
    }

    // 테마 카테고리(tno)로 상품 목록 조회
    public PageResponseDTO<ProductListDTO> listByTheme(Optional<Long> tno, PageRequestDTO pagerequestDTO) {
        log.info("테마 카테고리 ID(tno)로 상품 목록을 조회합니다: {}", tno);

        Pageable pageable = PageRequest.of(
                pagerequestDTO.getPage() - 1,
                pagerequestDTO.getSize(),
                Sort.by(Sort.Direction.DESC, "pno") // pno를 기준으로 내림차순 정렬
        );

        //테마 선택 여부에 따라 상품 조회
        Page<ProductListDTO> products = productRepository.findByThemeCategory(tno.orElse(null), pageable);

        return new PageResponseDTO<>(products.getContent(), pagerequestDTO, products.getTotalElements());
    }

    private ProductListDTO entityToDto(Product product) {
        return ProductListDTO.builder()
                .pno(product.getPno())
                .pname(product.getPname())
                .price(product.getPrice())
                .categoryCno(product.getCategory() != null ? product.getCategory().getCno() : null)
                .subCategoryScno(product.getSubCategory() != null ? product.getSubCategory().getScno() : null)
                .build();
    }


    // 상품 생성
    public Long createProduct(ProductListDTO productListDTO) {
        // Category와 SubCategory를 조회
        Category category = categoryRepository.findById(productListDTO.getCategoryCno())
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + productListDTO.getCategoryCno()));
        SubCategory subCategory = subCategoryRepository.findById(productListDTO.getSubCategoryScno())
                .orElseThrow(() -> new RuntimeException("SubCategory not found with ID: " + productListDTO.getSubCategoryScno()));

        // toEntity 호출 시 Category와 SubCategory 전달
        Product product = productListDTO.toEntity(category, subCategory);

        Product savedProduct = productRepository.save(product);
        log.info("Product created with ID: {}", savedProduct.getPno());
        return savedProduct.getPno();
    }

    // 상품 수정
    public Long updateProduct(Long pno, ProductListDTO productListDTO) {
        // Product 조회
        Product product = productRepository.findById(pno)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + pno));

        // Category와 SubCategory를 조회
        Category category = categoryRepository.findById(productListDTO.getCategoryCno())
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + productListDTO.getCategoryCno()));
        SubCategory subCategory = subCategoryRepository.findById(productListDTO.getSubCategoryScno())
                .orElseThrow(() -> new RuntimeException("SubCategory not found with ID: " + productListDTO.getSubCategoryScno()));

        // updateFromDTO 호출
        product.updateFromDTO(productListDTO, category, subCategory);

        productRepository.save(product);
        log.info("Product updated with ID: {}", pno);
        return pno;
    }

    // 상품 삭제
    public void deleteProduct(Long pno) {
        // Product 조회
        Product product = productRepository.findById(pno)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + pno));

        // 삭제 처리
        productRepository.delete(product);
        log.info("Product deleted with ID: {}", pno);
    }
}



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

