package com.tripwhiz.tripwhizuserback.category.dto;

import com.tripwhiz.tripwhizuserback.category.domain.Category;
import com.tripwhiz.tripwhizuserback.category.domain.SubCategory;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubCategoryDTO {

    private Long scno;

    @NotNull
    private String sname;


}
