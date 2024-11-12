package com.tripwhiz.tripwhizuserback.category.repository;

import com.tripwhiz.tripwhizuserback.category.domain.CategoryProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryProductRepository extends JpaRepository<CategoryProduct, Long> {



}