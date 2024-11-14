package com.tripwhiz.tripwhizuserback.product.domain;

import com.tripwhiz.tripwhizuserback.category.domain.Category;
import com.tripwhiz.tripwhizuserback.category.domain.SubCategory;
import com.tripwhiz.tripwhizuserback.category.domain.ThemeCategory;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pno;

    @Column(nullable = false, length = 50)
    private String pname;

    @Lob
    private String pdesc;

    private int price;


    private boolean delFlag;

    // Image 컬렉션을 List로 변경하고 정렬 인덱스 추가
    @ElementCollection
    @CollectionTable(name = "product_images")
    private List<Image> images = new ArrayList<>();

    // 상위 카테고리와의 관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cno") // 외래 키 이름을 지정 (상위 카테고리 ID와 연결)
    private Category category;

    // 하위 카테고리와의 관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scno") // 외래 키 이름을 지정 (하위 카테고리 ID와 연결)
    private SubCategory subCategory;

    // 테마 카테고리 설정 (예: 휴양, 힐링 등)
    @Enumerated(EnumType.STRING) // Enum 값을 데이터베이스에 문자열로 저장
    private ThemeCategory themeCategory;


    // 삭제 상태 변경 메서드
    public void changeDelFlag(boolean newDelFlag) {
        this.delFlag = newDelFlag;
    }

    public void addImage(String filename, String imageUrl) {
        images.add(new Image(images.size(), filename, imageUrl));  // ord 필드 설정
    }

    public void clearImages() {
        images.clear();
    }

    // 상위 카테고리 설정 메서드
    public void setCategory(Category category) {
        this.category = category;
    }

    // 하위 카테고리 설정 메서드
    public void setSubCategory(SubCategory subCategory) {
        this.subCategory = subCategory;
    }

    // 테마 카테고리 설정 메서드
    public void setThemeCategory(ThemeCategory themeCategory) {
        this.themeCategory = themeCategory;
    }
}
