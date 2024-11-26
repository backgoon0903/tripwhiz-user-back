package com.tripwhiz.tripwhizuserback.category.dto;

import com.tripwhiz.tripwhizuserback.category.domain.Category;
import com.tripwhiz.tripwhizuserback.category.domain.ParentCategory;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategoryDTO {

    private Long cno;

    private ParentCategory parentCategory;


}
