package com.tripwhiz.tripwhizuserback.product.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ino;

    private int ord;  // 고유 ID 필드로 설정
    private String fileName;  // 파일명


}