package com.tripwhiz.tripwhizuserback.product.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fid;  // 고유 ID 필드로 설정

    private String file;  // 파일명
    private String imageUrl;  // 이미지 URL

    // 기본 생성자
    public Image() {}

    // file과 imageUrl을 받는 생성자
    public Image(String file, String imageUrl) {
        this.file = file;
        this.imageUrl = imageUrl;
    }

    // Getters and Setters
    public Long getFid() {
        return fid;
    }

    public void setFid(Long fid) {
        this.fid = fid;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
