package com.tripwhiz.tripwhizuserback.product.repository;

import com.tripwhiz.tripwhizuserback.common.dto.PageRequestDTO;
import com.tripwhiz.tripwhizuserback.common.dto.PageResponseDTO;
import com.tripwhiz.tripwhizuserback.product.domain.Product;
import com.tripwhiz.tripwhizuserback.product.dto.ProductListDTO;
import com.tripwhiz.tripwhizuserback.product.dto.ProductReadDTO;
import com.tripwhiz.tripwhizuserback.product.repository.search.ProductSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductSearch {

//    전체 상품 목록 조회
    @Query("SELECT new com.tripwhiz.tripwhizuserback.product.dto.ProductListDTO(" +
            "p.pno, p.pname, p.price, p.category.cno, p.subCategory.scno) " +
            "FROM Product p " +
            "ORDER BY p.pno DESC")
    Page<ProductListDTO> allProductList(Pageable pageable);


    // 특정 상품을 ProductReadDTO 형태로 조회
    @Query("select new com.tripwhiz.tripwhizuserback.product.dto.ProductReadDTO(" +
            "p.pno, p.pname, p.pdesc, p.price, " +
            "p.category.cno, p.subCategory.scno) " +
            "from Product p " +
            "where p.pno = :pno")
    Optional<ProductReadDTO> read(@Param("pno") Long pno);



    // 상위 카테고리를 기준으로 상품 조회
    @Query("SELECT new com.tripwhiz.tripwhizuserback.product.dto.ProductListDTO(" +
           "p.pno, p.pname, p.price, p.category.cno, p.subCategory.scno) " +
           "FROM Product p " +
           "WHERE p.category.cno = :cno " +
           "ORDER BY p.pno DESC")
    Page<ProductListDTO> findByCategory(@Param("cno") Long cno, Pageable pageable);

    // 상위 & 하위 카테고리를 기준으로 상품 조회
    @Query("SELECT new com.tripwhiz.tripwhizuserback.product.dto.ProductListDTO(" +
           "p.pno, p.pname, p.price, p.category.cno, p.subCategory.scno) " +
           "FROM Product p " +
           "WHERE p.category.cno = :cno AND p.subCategory.scno = :scno " +
           "ORDER BY p.pno DESC")
    Page<ProductListDTO> findByCategoryAndSubCategory(@Param("cno") Long cno, @Param("scno") Long scno, Pageable pageable);


    // 테마 카테고리 tno를 기준으로 상품 조회
    @Query("SELECT new com.tripwhiz.tripwhizuserback.product.dto.ProductListDTO(" +
            "p.pno, p.pname, p.price, p.category.cno, p.subCategory.scno) " +
            "FROM Product p " +
            "LEFT JOIN ProductTheme pt ON p.pno = pt.product.pno " +
            "LEFT JOIN ThemeCategory tc ON pt.themeCategory.tno = tc.tno " +
            "WHERE (:tno IS NULL OR tc.tno = :tno) " +
            "ORDER BY p.pno DESC")
    Page<ProductListDTO> findByThemeCategory(@Param("tno") Long tno, Pageable pageable);


 }
