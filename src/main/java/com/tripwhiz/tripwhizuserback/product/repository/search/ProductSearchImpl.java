package com.tripwhiz.tripwhizuserback.product.repository.search;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import com.tripwhiz.tripwhizuserback.category.domain.QCategoryProduct;
import com.tripwhiz.tripwhizuserback.common.dto.PageRequestDTO;
import com.tripwhiz.tripwhizuserback.common.dto.PageResponseDTO;
import com.tripwhiz.tripwhizuserback.product.domain.Product;
import com.tripwhiz.tripwhizuserback.product.domain.QImage;
import com.tripwhiz.tripwhizuserback.product.domain.QProduct;
import com.tripwhiz.tripwhizuserback.product.dto.ProductListDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.tripwhiz.tripwhizuserback.product.domain.QImage.image;

@Log4j2
public class ProductSearchImpl extends QuerydslRepositorySupport implements ProductSearch {

    public ProductSearchImpl() {
        super(Product.class);
    }

    @Override
    public Page<Product> list(Pageable pageable) {
        log.info("-------------------list-----------");

        QProduct product = QProduct.product;
        QImage image = QImage.image;

        JPQLQuery<Product> query = from(product);
        query.leftJoin(product.images, image);

        query.where(image.ord.eq(0));
        query.groupBy(product);

        // 페이징 및 정렬 처리
        this.getQuerydsl().applyPagination(pageable, query);

        JPQLQuery<Tuple> tupleQuery = query.select(
                product,
                image.filename
        );

        tupleQuery.fetch();
        return null;
    }

    @Override
    public PageResponseDTO<ProductListDTO> listByCno(PageRequestDTO pageRequestDTO) {
        return listByCategory(pageRequestDTO.getCategoryCno(), pageRequestDTO);
    }

    @Override
    public PageResponseDTO<ProductListDTO> listByCategory(Long cno, PageRequestDTO pageRequestDTO) {
        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("pno").descending()
        );

        QProduct product = QProduct.product;
        QImage image = QImage.image;
        QCategoryProduct categoryProduct = QCategoryProduct.categoryProduct;

        JPQLQuery<Product> query = from(product);
        query.leftJoin(categoryProduct).on(categoryProduct.product.eq(product));
        query.leftJoin(product.images, image);

        query.where(image.ord.eq(0));
        if (cno != null) {
            query.where(product.category.cno.eq(cno));
        }

        query.groupBy(product);

        this.getQuerydsl().applyPagination(pageable, query);

        JPQLQuery<Tuple> tupleQuery = query.select(
                product,
                product.fileUrl,
                product.count()
        );

        List<Tuple> tupleList = tupleQuery.fetch();
        if (tupleList.isEmpty()) {
            return null;
        }

        List<ProductListDTO> dtoList = tupleList.stream().map(t -> {
            Product productObj = t.get(product);
            String fileUrl = t.get(product.fileUrl);

            return ProductListDTO.builder()
                    .pno(productObj.getPno())
                    .pname(productObj.getPname())
                    .price(productObj.getPrice())
                    .fileUrl(fileUrl)
                    .build();
        }).collect(Collectors.toList());

        long total = tupleQuery.fetchCount();

        return PageResponseDTO.<ProductListDTO>withAll()
                .dtoList(dtoList)
                .totalCount(total)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }

    @Override
    public PageResponseDTO<ProductListDTO> listBySubCategory(Long scno, PageRequestDTO pageRequestDTO) {
        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("pno").descending()
        );

        QProduct product = QProduct.product;
        QImage image = QImage.image;
        JPQLQuery<Product> query = from(product);

        if (scno != null) {
            query.where(product.subCategory.scno.eq(scno));
        }

        query.groupBy(product);

        this.getQuerydsl().applyPagination(pageable, query);

        // 쿼리 수정: count()를 추가하여 총 개수와 파일명 가져오기
        JPQLQuery<Tuple> tupleQuery = query.select(
                product,
                image.filename,
                product.count()
        );

        List<Tuple> tupleList = tupleQuery.fetch();
        log.info(tupleList);

        if (tupleList.isEmpty()) {
            return null;
        }

        List<ProductListDTO> dtoList = new ArrayList<>();

        tupleList.forEach(t -> {
            Product productObj = t.get(product); // QProduct에서 Product 객체 직접 추출
            String fileName = t.get(image.filename);
            Long count = t.get(product.count()); // count 값 추출

            ProductListDTO dto = ProductListDTO.builder()
                    .pno(productObj.getPno())
                    .pname(productObj.getPname())
                    .price(productObj.getPrice())
                    .fileUrl(fileName)
                    .build();

            dtoList.add(dto); // dtoList에 추가
        });

        long total = tupleQuery.fetchCount();

        return PageResponseDTO.<ProductListDTO>withAll()
                .dtoList(dtoList)
                .totalCount(total)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }

    @Override
    public PageResponseDTO<ProductListDTO> listByTheme(String themeCategory, PageRequestDTO pageRequestDTO) {
        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("pno").descending()
        );

        QProduct product = QProduct.product;
        JPQLQuery<Product> query = from(product);

        if (themeCategory != null) {
            query.where(product.themeCategory.stringValue().eq(themeCategory));
        }

        query.groupBy(product);

        this.getQuerydsl().applyPagination(pageable, query);

        JPQLQuery<Tuple> tupleQuery = query.select(
                product,
                product.fileUrl,
                product.count()
        );

        List<Tuple> tupleList = tupleQuery.fetch();
        if (tupleList.isEmpty()) {
            return null;
        }

        List<ProductListDTO> dtoList = tupleList.stream().map(t -> {
            Product productObj = t.get(product);
            String fileUrl = t.get(product.fileUrl);

            return ProductListDTO.builder()
                    .pno(productObj.getPno())
                    .pname(productObj.getPname())
                    .price(productObj.getPrice())
                    .fileUrl(fileUrl)
                    .build();
        }).collect(Collectors.toList());

        long total = tupleQuery.fetchCount();

        return PageResponseDTO.<ProductListDTO>withAll()
                .dtoList(dtoList)
                .totalCount(total)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }

}