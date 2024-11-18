package com.tripwhiz.tripwhizuserback.product.repository;

import com.tripwhiz.tripwhizuserback.category.domain.CategoryProduct;
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
            "p.pno, p.pname, p.price, p.category.cno, p.subCategory.scno, t.tno, i.fileName) " +
            "FROM Product p " +
            "LEFT JOIN p.images i ON i.ord = 0 " +
            "LEFT JOIN p.themeCategory t ON t.tno = p.themeCategory.tno " +
            "ORDER BY p.pno DESC")
    Page<ProductListDTO> allProductList(Pageable pageable);


    // 특정 상품을 ProductReadDTO 형태로 조회
    @Query("select new com.tripwhiz.tripwhizuserback.product.dto.ProductReadDTO(" +
            "p.pno, p.pname, p.pdesc, p.price, " +
            "p.category.cno, p.subCategory.scno, t.tno, i.fileName) " +
            "from Product p " +
            "left join p.images i on i.ord = 0 " +
            "left join p.themeCategory t " +
            "where p.pno = :pno")
    Optional<ProductReadDTO> read(@Param("pno") Long pno);

    // 특정 상품의 CategoryProduct 정보를 조회
    @Query("select cp from CategoryProduct cp " +
            "join fetch cp.product p " +
            "join fetch cp.category c " +
            "where p.pno = :pno")
    Optional<CategoryProduct> findByCategory(@Param("pno") Long pno);

    // 상위 카테고리를 기준으로 상품 조회
    @Query("SELECT new com.tripwhiz.tripwhizuserback.product.dto.ProductListDTO(" +
            "p.pno, p.pname, p.price, p.category.cno, p.subCategory.scno, t.tno, i.fileName) " +
            "FROM Product p " +
            "LEFT JOIN p.images i ON i.ord = 0 " +
            "LEFT JOIN p.themeCategory t ON t.tno = p.themeCategory.tno " +
            "WHERE p.category.cno = :cno " +
            "ORDER BY p.pno DESC")
    Page<ProductListDTO> findByCategoryCno(@Param("cno") Long cno, Pageable pageable);

    // 하위 카테고리를 기준으로 상품 조회
    @Query("SELECT new com.tripwhiz.tripwhizuserback.product.dto.ProductListDTO(" +
            "p.pno, p.pname, p.price, p.category.cno, p.subCategory.scno, t.tno, i.fileName) " +
            "FROM Product p " +
            "LEFT JOIN p.images i ON i.ord = 0 " +
            "LEFT JOIN p.themeCategory t ON t.tno = p.themeCategory.tno " +
            "WHERE p.subCategory.scno = :scno " +
            "ORDER BY p.pno DESC")
    Page<ProductListDTO> findBySubCategoryScno(@Param("scno") Long scno, Pageable pageable);

    // 테마 카테고리 tno를 기준으로 상품 조회
    @Query("SELECT new com.tripwhiz.tripwhizuserback.product.dto.ProductListDTO(" +
            "p.pno, p.pname, p.price, p.category.cno, p.subCategory.scno, t.tno, i.fileName) " +
            "FROM Product p " +
            "LEFT JOIN p.images i ON i.ord = 0 " +
            "LEFT JOIN p.themeCategory t ON t.tno = p.themeCategory.tno " +
            "WHERE t.tno = :tno " +
            "ORDER BY p.pno DESC")
    Page<ProductListDTO> findByThemeCategoryTno(@Param("tno") Long tno, Pageable pageable);

 }
