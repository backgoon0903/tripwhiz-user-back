package com.tripwhiz.tripwhizuserback.category.dto;

import com.tripwhiz.tripwhizuserback.category.domain.Category;
import com.tripwhiz.tripwhizuserback.category.domain.ParentCategory;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategoryDTO {

    private Long cno;

    private ParentCategory category;

    public static CategoryDTO fromEntity(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setCno(category.getCno());
        dto.setCategory(category.getCategory());
        return dto;
    }

}
