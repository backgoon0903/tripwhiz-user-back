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

    @Query("select " +
            "new com.tripwhiz.tripwhizuserback.product.dto.ProductReadDTO(p.pno, p.pname, p.pdesc, p.price) " +
            "from Product p where p.pno = :pno")
    Optional<ProductReadDTO> read(@Param("pno") Long pno);

    @Query("select cp from CategoryProduct cp " +
            "join fetch cp.product p " +
            "join fetch cp.category c " +
            "where p.pno = :pno")
    Optional<CategoryProduct> findCategory(@Param("pno") Long pno);

}
