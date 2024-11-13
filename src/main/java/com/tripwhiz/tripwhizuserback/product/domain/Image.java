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


    private int ord;  // 기존 id를 fid로 변경
    private String file;  // 기존 filename을 file로 변경
    private String imageUrl;

    // file과 imageUrl을 받는 생성자 추가
    public Image(String file, String imageUrl) {
        this.file = file;
        this.imageUrl = imageUrl;
    }

}