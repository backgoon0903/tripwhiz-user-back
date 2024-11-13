package com.tripwhiz.tripwhizuserback.product.domain;

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
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    private List<Image> images = new ArrayList<>();

    public void changeDelFlag(boolean newDelFlag) {
        this.delFlag = newDelFlag;
    }

    public void addImage(String filename, String imageUrl) {
        images.add(new Image(images.size(), filename, imageUrl));  // ord 필드 설정
    }

    public void clearImages() {
        images.clear();
    }
}
