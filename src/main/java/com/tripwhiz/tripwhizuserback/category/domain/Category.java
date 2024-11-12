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
    private Long cno;

    @Column(nullable = false, length = 50)
    private String cname;

    @Column(nullable = false)
    private String theme;

    private ParentCategory category;

    private boolean delFlag;

}
