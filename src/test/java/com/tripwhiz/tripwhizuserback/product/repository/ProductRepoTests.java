package com.tripwhiz.tripwhizuserback;

import com.tripwhiz.tripwhizuserback.category.domain.Category;
import com.tripwhiz.tripwhizuserback.category.repository.CategoryRepository;
import com.tripwhiz.tripwhizuserback.category.domain.CategoryProduct;
import com.tripwhiz.tripwhizuserback.category.repository.CategoryProductRepository;
import com.tripwhiz.tripwhizuserback.product.domain.AttachFile;
import com.tripwhiz.tripwhizuserback.product.domain.Product;
import com.tripwhiz.tripwhizuserback.product.repository.ProductRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Log4j2

public class ProductRepoTests {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryProductRepository categoryProductRepository;

    @BeforeEach
    @Transactional
    @Commit
    public void setUp() {
        // 카테고리 10개 생성 및 저장
        IntStream.rangeClosed(1, 10).forEach(i -> {
            Category category = Category.builder()
                    .dname("카테고리 " + i)
                    .delFlag(false)
                    .build();
            categoryRepository.save(category);

            // 각 카테고리에 연관된 프로덕트 2개 생성 및 저장
            IntStream.rangeClosed(1, 2).forEach(j -> {
                Product product = Product.builder()
                        .pname("상품 " + j + " in " + category.getDname())
                        .pdesc("설명 " + j)
                        .price(1000 * j)
                        .delFlag(false)
                        .attachFiles(new HashSet<>(Set.of(new AttachFile(0, "file" + j + ".jpg"))))
                        .build();
                productRepository.save(product);

                // CategoryProduct 엔티티 생성 및 저장
                CategoryProduct categoryProduct = CategoryProduct.builder()
                        .category(category)
                        .product(product)
                        .build();
                categoryProductRepository.save(categoryProduct);
                log.info("Inserted CategoryProduct: " + categoryProduct + " for Category: " + category.getDname() + " and Product: " + product.getPname());
            });
        });
    }

    @Test
    @Transactional
    public void testCategoryProductInsertion() {
        long categoryCount = categoryRepository.count();
        long productCount = productRepository.count();
        long categoryProductCount = categoryProductRepository.count();

        assertThat(categoryCount).isEqualTo(10);
        assertThat(productCount).isEqualTo(20);
        assertThat(categoryProductCount).isEqualTo(20);

        log.info("Total Categories in DB: " + categoryCount);
        log.info("Total Products in DB: " + productCount);
        log.info("Total CategoryProducts in DB: " + categoryProductCount);
    }
}
