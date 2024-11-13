package com.tripwhiz.tripwhizuserback.product.dto;

import com.tripwhiz.tripwhizuserback.category.domain.ThemeCategory;
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
    private String fileName;// 상품 가격
    private String fileUrl;        // 외부 파일 URL
    private Long categoryCno;      // 상위 카테고리 ID
    private Long subCategoryScno;  // 하위 카테고리 ID
    private ThemeCategory themeCategory;// 테마 카테고리
    private String fileName;
}
