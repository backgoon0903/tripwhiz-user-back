package com.tripwhiz.tripwhizuserback.product.domain;

import com.tripwhiz.tripwhizuserback.category.domain.Category;
import com.tripwhiz.tripwhizuserback.category.domain.SubCategory;
import com.tripwhiz.tripwhizuserback.category.domain.ThemeCategory;
import jakarta.persistence.*;
import lombok.*;

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

    // 외부 파일 URL을 저장하는 필드
    private String fileUrl;

    private boolean delFlag;

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

    // 파일 URL 설정 메서드
    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
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
