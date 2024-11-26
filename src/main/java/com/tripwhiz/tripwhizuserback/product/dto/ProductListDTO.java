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
    private String pdesc;
    private Long categoryCno;      // 상위 카테고리 ID
    private Long subCategoryScno;  // 하위 카테고리 ID


    public Product toEntity(Category category, SubCategory subCategory) {
        return Product.builder()
                .pname(this.pname)
                .price(this.price)
                .pdesc(this.pdesc)
                .category(category)  // Category 객체를 직접 설정
                .subCategory(subCategory)  // SubCategory 객체를 직접 설정
                .build();
    }
    public ProductListDTO(Long pno, String pdesc, String pname, int price, Long categoryCno, Long subCategoryScno) {
        this.pno = pno;
        this.pdesc = pdesc;
        this.pname = pname;
        this.price = price;
        this.categoryCno = categoryCno;
        this.subCategoryScno = subCategoryScno;
    }

}
