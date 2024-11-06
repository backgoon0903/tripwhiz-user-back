package com.tripwhiz.tripwhizuserback.category.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Builder
public class ParentCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cno;

    @Column(nullable = false, length = 50)
    private String theme;

    @Column(nullable = false, length = 50)
    private String cname;

    private boolean delFlag = false;

}
