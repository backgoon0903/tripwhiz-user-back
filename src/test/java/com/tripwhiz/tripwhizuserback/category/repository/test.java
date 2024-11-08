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

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Log4j2
public class test {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryProductRepository categoryProductRepository;

    private final Random random = new Random();

    @Test
    @Transactional
    @Commit
    public void testInsertCategoriesProductsAndCategoryProducts() {
        // 1. 카테고리 10개 생성 및 저장
        IntStream.rangeClosed(1, 10).forEach(i -> {
            Category category = Category.builder()
                    .dname("카테고리 " + i)
                    .delFlag(false)
                    .build();
            categoryRepository.save(category);
            log.info("Inserted Category: " + category);
        });

        // 2. 프로덕트 100개 생성 및 저장
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Product product = Product.builder()
                    .pname("상품 " + i)
                    .pdesc("설명 " + i)
                    .price(1000 * i)
                    .delFlag(false)
                    .attachFiles(Set.of(new AttachFile(0, "file" + i + ".jpg")))
                    .build();
            productRepository.save(product);
            log.info("Inserted Product: " + product);
        });

        // 3. 모든 카테고리와 프로덕트를 조회하여 각각 하나씩 연결
        List<Category> categories = categoryRepository.findAll();
        List<Product> products = productRepository.findAll();

        products.forEach(product -> {
            // Product마다 랜덤한 1개의 Category와 연결
            Category category = categories.get(random.nextInt(categories.size()));

            // CategoryProduct 생성 및 저장
            CategoryProduct categoryProduct = CategoryProduct.builder()
                    .category(category)
                    .product(product)
                    .build();
            categoryProductRepository.save(categoryProduct);

            log.info("Linked Product: " + product.getPname() + " with Category: " + category.getDname());
        });

        // 4. 검증
        long categoryCount = categoryRepository.count();
        long productCount = productRepository.count();
        long categoryProductCount = categoryProductRepository.count();

        assertThat(categoryCount).isEqualTo(10);
        assertThat(productCount).isEqualTo(100);
        assertThat(categoryProductCount).isEqualTo(100); // Product마다 하나의 Category 연결 확인

        log.info("Total Categories in DB: " + categoryCount);
        log.info("Total Products in DB: " + productCount);
        log.info("Total CategoryProduct entries in DB: " + categoryProductCount);
    }
}
