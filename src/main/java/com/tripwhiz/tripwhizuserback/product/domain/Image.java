package com.tripwhiz.tripwhizuserback.product.domain;


import jakarta.persistence.*;

@Embeddable
public class Image {

    @Id
    private Long fid;  // 기존 id를 fid로 변경

    private String file;  // 기존 filename을 file로 변경
    private String imageUrl;

    // 기본 생성자
    public Image() {}

    // file과 imageUrl을 받는 생성자 추가
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
