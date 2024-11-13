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

    @Builder.Default
    private ParentCategory category = ParentCategory.All;

    private boolean delFlag;

    @Builder.Default
    private ThemeCategory themeCategory = ThemeCategory.RELAXATION;

}
