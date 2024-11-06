package com.tripwhiz.tripwhizuserback.category.domain;

import jakarta.persistence.*;
import lombok.*;
import com.tripwhiz.tripwhizuserback.product.domain.Product;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"product","category"})
public class CategoryProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cpno;

    @ManyToOne
    private Product product;

    @ManyToOne
    private Category category;

}
