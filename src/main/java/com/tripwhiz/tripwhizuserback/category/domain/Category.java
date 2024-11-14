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

    @Builder.Default
    private ParentCategory category = ParentCategory.All;

    @Builder.Default
    private ThemeCategory themeCategory = ThemeCategory.RELAXATION;

    private boolean delFlag;

}
