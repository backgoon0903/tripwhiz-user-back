package com.tripwhiz.tripwhizuserback.product.repository.search;

import com.querydsl.jpa.JPQLQuery;
import com.tripwhiz.tripwhizuserback.common.dto.PageRequestDTO;
import com.tripwhiz.tripwhizuserback.product.domain.Product;
import com.tripwhiz.tripwhizuserback.product.domain.QProduct;
import com.tripwhiz.tripwhizuserback.product.domain.QProductTheme;
import com.tripwhiz.tripwhizuserback.product.domain.QThemeCategory;
import com.tripwhiz.tripwhizuserback.product.dto.ProductListDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Log4j2
public class ProductSearchImpl extends QuerydslRepositorySupport implements ProductSearch {

    public ProductSearchImpl() {
        super(Product.class);
    }

    //전체 상품 목록 조회(다 스킵했을 때)
    @Override
    public Page<ProductListDTO> list(PageRequestDTO pageRequestDTO) {
        log.info("-------------------list-----------");

        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("pno").descending()
        );

        QProduct product = QProduct.product;
        JPQLQuery<Product> query = from(product);

        query.leftJoin(product.attachFiles).fetchJoin();
        query.groupBy(product);

        // 페이징 적용
        List<Product> productList = getQuerydsl().applyPagination(pageable, query)
                .select(product)
                .fetch();

        // DTO 변환
        List<ProductListDTO> dtoList = productList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        // 총 개수 조회
        long total = query.fetchCount();

        return new PageImpl<>(dtoList, pageable, total);
    }

    @Override
    public Page<ProductListDTO> findByCategory(Long cno, PageRequestDTO pageRequestDTO) {

        log.info("Fetching product list by category ID: {}", cno);

        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() - 1, // 클라이언트는 1부터 시작하지만 Pageable은 0부터 시작하므로 -1
                pageRequestDTO.getSize(),  // 페이지 크기 설정
                Sort.by("pno").descending()
        );

        // Querydsl로 Product 엔티티를 조회하기 위해 QProduct 객체 생성
        QProduct product = QProduct.product;

        JPQLQuery<Product> query = from(product)
                .leftJoin(product.attachFiles).fetchJoin()
                .where(product.category.cno.eq(cno))
                .groupBy(product);

        // 페이징 적용
        List<Product> productList = getQuerydsl().applyPagination(pageable, query)
                .select(product)
                .fetch();

        // DTO 변환
        List<ProductListDTO> dtoList = productList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        // 총 개수 조회
        long total = query.fetchCount();

        return new PageImpl<>(dtoList, pageable, total);
    }

    @Override
    public Page<ProductListDTO> findByCategoryAndSubCategory(Long cno, Long scno, PageRequestDTO pageRequestDTO) {

        log.info("Fetching product list by category ID: {} and sub-category ID: {}", cno, scno);

        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("pno").descending()
        );

        QProduct product = QProduct.product;

        JPQLQuery<Product> query = from(product)
                .leftJoin(product.attachFiles).fetchJoin()
                .where(product.category.cno.eq(cno).and(product.subCategory.scno.eq(scno)));

        // 페이징 적용
        List<Product> productList = getQuerydsl().applyPagination(pageable, query)
                .select(product)
                .fetch();

        // DTO 변환
        List<ProductListDTO> dtoList = productList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        // 총 개수 조회
        long total = query.fetchCount();

        return new PageImpl<>(dtoList, pageable, total);
    }


    @Override
    public Page<ProductListDTO> findByThemeCategory(Optional<Long> tno, PageRequestDTO pageRequestDTO) {

        log.info("Fetching product list by theme category ID: {}", tno);

        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("pno").descending()
        );

        QProduct product = QProduct.product;
        QProductTheme productTheme = QProductTheme.productTheme;
        QThemeCategory themeCategory = QThemeCategory.themeCategory;

        // Product와 ThemeCategory를 연결하는 쿼리 작성
        JPQLQuery<Product> query = from(product)
                .leftJoin(product.attachFiles).fetchJoin()
                .innerJoin(productTheme).on(product.eq(productTheme.product))
                .innerJoin(themeCategory).on(productTheme.themeCategory.eq(themeCategory));

        //tno가 존재하면 필터 조건 추가
        tno.ifPresent(theme -> query.where(themeCategory.tno.eq(theme)));


        // 페이징 적용 및 데이터 조회
        List<Product> productList = getQuerydsl().applyPagination(pageable, query)
                .select(product)
                .fetch();

        // DTO 변환
        List<ProductListDTO> dtoList = productList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        // 총 개수 조회
        long total = query.fetchCount();

        return new PageImpl<>(dtoList, pageable, total);
    }


    private ProductListDTO convertToDto(Product product) {
        return ProductListDTO.builder()
                .pno(product.getPno())
                .pname(product.getPname())
                .price(product.getPrice())
                .categoryCno(product.getCategory() != null ? product.getCategory().getCno() : null)
                .subCategoryScno(product.getSubCategory() != null ? product.getSubCategory().getScno() : null)
                .build();
    }


}
