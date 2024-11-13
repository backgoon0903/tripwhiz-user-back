package com.tripwhiz.tripwhizuserback.category.repository;

import com.tripwhiz.tripwhizuserback.category.domain.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
    List<SubCategory> findByCategory_Cno(Long cno);

}
