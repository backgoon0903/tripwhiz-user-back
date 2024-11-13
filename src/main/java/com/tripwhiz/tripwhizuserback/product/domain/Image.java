package com.tripwhiz.tripwhizuserback.product.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Image {


    private int ord;  // 고유 ID 필드로 설정

    private String filename;  // 파일명
    private String imageUrl;  // 이미지 URL

}