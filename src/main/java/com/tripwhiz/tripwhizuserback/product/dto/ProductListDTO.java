package com.tripwhiz.tripwhizuserback.product.dto;

import com.tripwhiz.tripwhizuserback.category.domain.Category;
import com.tripwhiz.tripwhizuserback.category.domain.SubCategory;
import com.tripwhiz.tripwhizuserback.product.domain.ThemeCategory;
import com.tripwhiz.tripwhizuserback.product.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductListDTO {

    private Long pno;             // 상품 번호
    private String pname;          // 상품 이름
    private int price;
    private Long categoryCno;      // 상위 카테고리 ID
    private Long subCategoryScno;  // 하위 카테고리 ID
    private Long themeTno; // 테마 카테고리 ID
    private String fileName;

    public Product toEntity(Category category, SubCategory subCategory, ThemeCategory themeCategory) {
        return Product.builder()
                .pno(this.pno)
                .pname(this.pname)
                .price(this.price)
                .category(category)  // Category 객체를 직접 설정
                .subCategory(subCategory)  // SubCategory 객체를 직접 설정
                .themeCategory(themeCategory)
                .build();
    }

}
