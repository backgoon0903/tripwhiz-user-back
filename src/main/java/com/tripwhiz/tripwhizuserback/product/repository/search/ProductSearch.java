package com.tripwhiz.tripwhizuserback.product.repository.search;

import com.tripwhiz.tripwhizuserback.common.dto.PageRequestDTO;
import com.tripwhiz.tripwhizuserback.product.dto.ProductListDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


public interface ProductSearch {

    // 전체 상품 목록 조회
    Page<ProductListDTO> list(PageRequestDTO pageRequestDTO);

    // 상위 카테고리 cno로 상품 목록 조회
    Page<ProductListDTO> findByCategory(Long cno, PageRequestDTO pageRequestDTO);

    // cno와 하위 카테고리 scno로 상품 목록 조회
    Page<ProductListDTO> findByCategoryAndSubCategory(Long cno, Long scno, PageRequestDTO pageRequestDTO);

    // 테마 카테고리 tno로 상품 목록 조회
    Page<ProductListDTO> findByThemeCategory(Optional<Long> tno, PageRequestDTO pageRequestDTO);
}


