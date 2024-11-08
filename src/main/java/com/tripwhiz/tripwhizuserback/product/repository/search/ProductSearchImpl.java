package com.tripwhiz.tripwhizuserback.product.repository.search;

import com.querydsl.jpa.JPQLQuery;
import com.tripwhiz.tripwhizuserback.product.domain.Product;
import com.tripwhiz.tripwhizuserback.product.domain.QAttachFile;
import com.tripwhiz.tripwhizuserback.product.domain.QProduct;
import com.tripwhiz.tripwhizuserback.product.dto.ProductReadDTO;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.Optional;

public class ProductSearchImpl extends QuerydslRepositorySupport implements ProductSearch {

    public ProductSearchImpl() {
        super(Product.class);
    }

    @Override
    public Optional<ProductReadDTO> read(Long pno) {

        QProduct product = QProduct.product;
//        QAttachFile attachFile = QAttachFile.attachFile;

        JPQLQuery<Product> query = from(product)
//                .leftJoin(product.attachFiles, attachFile)
                .where(product.pno.eq(pno));

        Product productEntity = query.fetchOne();

        if (productEntity == null) {

            return Optional.empty();

        }

        ProductReadDTO dto = ProductReadDTO.builder()
                .pno(productEntity.getPno())
                .pname(productEntity.getPname())
                .pdesc(productEntity.getPdesc())
                .price(productEntity.getPrice())
//                .attachFiles(productEntity.getAttachFiles())
                .build();

        return Optional.of(dto);

    }

}
