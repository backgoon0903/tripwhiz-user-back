package com.tripwhiz.tripwhizuserback.product.repository.search;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import com.tripwhiz.tripwhizuserback.category.domain.QCategoryProduct;
import com.tripwhiz.tripwhizuserback.common.dto.PageRequestDTO;
import com.tripwhiz.tripwhizuserback.common.dto.PageResponseDTO;
import com.tripwhiz.tripwhizuserback.product.domain.Product;
import com.tripwhiz.tripwhizuserback.product.domain.QAttachFile;
import com.tripwhiz.tripwhizuserback.product.domain.QProduct;
import com.tripwhiz.tripwhizuserback.product.dto.ProductListDTO;
import com.tripwhiz.tripwhizuserback.product.dto.ProductReadDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
public class ProductSearchImpl extends QuerydslRepositorySupport implements ProductSearch {

    public ProductSearchImpl() {
        super(Product.class);
    }

    @Override
    public Page<Product> list(Pageable pageable) {

        log.info("---------list------------");

        QProduct product = QProduct.product;
        QAttachFile attachFile = QAttachFile.attachFile;

        JPQLQuery<Product> query = from(product);
        query.leftJoin(product.attachFiles, attachFile);

        query.where(attachFile.ord.eq(0));
        query.groupBy(product);

        this.getQuerydsl().applyPagination(pageable, query);

        JPQLQuery<Tuple> tupleQuery =
                query.select(
                        product,
                        attachFile.filename
                );

        tupleQuery.fetch();

        return null;
    }

    @Override
    public PageResponseDTO<ProductListDTO> listByCno(Long dno, PageRequestDTO pageRequestDTO) {

        Pageable pageable =
                PageRequest.of(
                        pageRequestDTO.getPage() -1,
                        pageRequestDTO.getSize(),
                        Sort.by("pno").descending()
                );

        QProduct product = QProduct.product;
        QAttachFile attachFile = QAttachFile.attachFile;
        QCategoryProduct categoryProduct = QCategoryProduct.categoryProduct;

        JPQLQuery<Product> query = from(product);

        query.leftJoin(categoryProduct).on(categoryProduct.product.eq(product));
        query.leftJoin(product.attachFiles, attachFile);

        query.where(attachFile.ord.eq(0));
        query.where(categoryProduct.category.dno.eq(dno));
        query.groupBy(product);

        JPQLQuery<Tuple> tupleQuery =
                query.select(
                        product,
                        attachFile.filename
                );

        List<Tuple> tupleList = tupleQuery.fetch();

        log.info(tupleList);

        if(tupleList.isEmpty()) {
            return null;
        }

        List<ProductListDTO> dtoList = new ArrayList<>();

        tupleList.forEach(t -> {
            Product productObj = t.get(0, Product.class);
            String filename = t.get(1, String.class);

            ProductListDTO dto = ProductListDTO.builder()
                    .pno(productObj.getPno())
                    .pname(productObj.getPname())
                    .fileName(filename)
                    .build();

            dtoList.add(dto);
        });

        long total = tupleQuery.fetchCount();

        return PageResponseDTO.<ProductListDTO>withAll()
                .dtoList(dtoList)
                .totalCount(total)
                .pageRequestDTO(pageRequestDTO)
                .build();

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
