package com.tripwhiz.tripwhizuserback.category.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dno;

    @Column(nullable = false, length = 50)
    private String dname;

    private boolean delFlag;

    // 여기에 부모카테고리 번호 추가해야 함
    // ParentCategory와 Many-to-One 관계 설정
    @ManyToOne
    @JoinColumn(name = "parent_cno") // 외래 키 이름 설정
    private ParentCategory parentCategory;

}
