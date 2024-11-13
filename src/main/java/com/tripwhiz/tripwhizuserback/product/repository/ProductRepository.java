package com.tripwhiz.tripwhizuserback.product.repository;

import com.tripwhiz.tripwhizuserback.category.domain.CategoryProduct;
import com.tripwhiz.tripwhizuserback.product.domain.Product;
import com.tripwhiz.tripwhizuserback.product.dto.ProductReadDTO;
import com.tripwhiz.tripwhizuserback.product.repository.search.ProductSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductSearch {

    // 특정 상품을 ProductReadDTO 형태로 조회
    @Query("select " +
            "new com.tripwhiz.tripwhizuserback.product.dto.ProductReadDTO(p.pno, p.pname, p.pdesc, p.price, p.fileUrl, p.category.cno, p.subCategory.scno, p.themeCategory) " +
            "from Product p " +
            "where p.pno = :pno")
    Optional<ProductReadDTO> read(@Param("pno") Long pno);

    // 특정 상품의 CategoryProduct 정보를 조회
    @Query("select cp from CategoryProduct cp " +
            "join fetch cp.product p " +
            "join fetch cp.category c " +
            "where p.pno = :pno")
    Optional<CategoryProduct> findCategory(@Param("pno") Long pno);

    // 상위 카테고리를 기준으로 상품 조회
    @Query("select p from Product p " +
            "join p.category c " +
            "where c.cno = :cno")
    Optional<Product> findByCategoryCno(@Param("cno") Long cno);

    // 하위 카테고리를 기준으로 상품 조회
    @Query("select p from Product p " +
            "join p.subCategory sc " +
            "where sc.scno = :scno")
    Optional<Product> findBySubCategoryScno(@Param("scno") Long scno);

    // 테마 카테고리를 기준으로 상품 조회
    @Query("select p from Product p " +
            "where p.themeCategory = :themeCategory")
    Optional<Product> findByThemeCategory(@Param("themeCategory") String themeCategory);
}
