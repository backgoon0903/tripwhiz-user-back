package com.tripwhiz.tripwhizuserback;

import com.tripwhiz.tripwhizuserback.category.domain.Category;
import com.tripwhiz.tripwhizuserback.category.domain.CategoryProduct;
import com.tripwhiz.tripwhizuserback.category.repository.CategoryProductRepository;
import com.tripwhiz.tripwhizuserback.category.repository.CategoryRepository;
import com.tripwhiz.tripwhizuserback.product.domain.AttachFile;
import com.tripwhiz.tripwhizuserback.product.domain.Product;
import com.tripwhiz.tripwhizuserback.product.repository.ProductRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Log4j2
public class CategoryRepoTests {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryProductRepository categoryProductRepository;

    @Test
    @Transactional
    @Commit
    public void testInsertCategoryProductRelation() {
        // 카테고리 생성 및 저장
        Category category = Category.builder()
                .dname("전자기기")
                .delFlag(false)
                .build();
        categoryRepository.save(category);
        log.info("Inserted Category: " + category);

        // 프로덕트 생성 및 저장
        Product product = Product.builder()
                .pname("스마트폰")
                .pdesc("최신 스마트폰 모델")
                .price(1000000)
                .delFlag(false)
                .attachFiles(new HashSet<>(Set.of(new AttachFile(0, "smartphone.jpg"))))
                .build();
        productRepository.save(product);
        log.info("Inserted Product: " + product);

        // 카테고리와 프로덕트를 연결하는 CategoryProduct 생성 및 저장
        CategoryProduct categoryProduct = CategoryProduct.builder()
                .category(category)
                .product(product)
                .build();
        categoryProductRepository.save(categoryProduct);
        log.info("Inserted CategoryProduct: " + categoryProduct + " with Category: " + category.getDname() + " and Product: " + product.getPname());

        // 검증: CategoryProduct가 데이터베이스에 잘 저장되었는지 확인
        long categoryProductCount = categoryProductRepository.count();
        assertThat(categoryProductCount).isEqualTo(1);
        log.info("Total CategoryProduct in DB: " + categoryProductCount);
    }
}
