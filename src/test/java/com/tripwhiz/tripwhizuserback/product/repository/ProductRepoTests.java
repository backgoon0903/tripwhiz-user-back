package com.tripwhiz.tripwhizuserback.product.repository;

import com.tripwhiz.tripwhizuserback.category.domain.Category;
import com.tripwhiz.tripwhizuserback.category.domain.ParentCategory;
import com.tripwhiz.tripwhizuserback.category.repository.CategoryRepository;
import com.tripwhiz.tripwhizuserback.product.domain.Image;
import com.tripwhiz.tripwhizuserback.product.domain.Product;
import com.tripwhiz.tripwhizuserback.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Commit;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Log4j2
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductRepoTests {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    private final Random random = new Random();

//    @Test
//    @Transactional
//    @Commit
//    public void testInsertCategoriesAndProducts() {
//        log.info("--------------- Start Test ---------------");
//
//        // 1. ParentCategory Enum을 기반으로 카테고리 생성 및 저장
//        for (ParentCategory parentCategory : ParentCategory.values()) {
//            Category category = Category.builder()
//                    .category(parentCategory)
//                    .delFlag(false)
//                    .build();
//            categoryRepository.save(category);
//            log.info("Inserted Category: {}", category);
//        }
//
//        // 2. 카테고리 조회
//        List<Category> categories = categoryRepository.findAll();
//        assertThat(categories.size()).isEqualTo(ParentCategory.values().length);
//
//        // 3. 상품 100개 생성 및 저장
//        IntStream.rangeClosed(1, 100).forEach(i -> {
//            Product product = Product.builder()
//                    .pname("상품 " + i)
//                    .pdesc("설명 " + i)
//                    .price(1000 * i)
//                    .delFlag(false)
//                    .build();
//            productRepository.save(product);
//            log.info("Inserted Product: {}", product);
//        });
//
//        // 4. 모든 카테고리와 상품을 랜덤하게 연결
//        List<Product> products = productRepository.findAll();
//        assertThat(products.size()).isEqualTo(100);
//
//        // 카테고리별로 랜덤하게 상품을 할당할 수 있습니다.
//        products.forEach(product -> {
//            Category category = categories.get(random.nextInt(categories.size()));
//
//            log.info("Linked Product '{}' with Category '{}'", product.getPname(), category.getCategory());
//        });
//
//        // 5. 검증
//        long categoryCount = categoryRepository.count();
//        long productCount = productRepository.count();
//
//        assertThat(categoryCount).isEqualTo(ParentCategory.values().length);
//        assertThat(productCount).isEqualTo(100);
//
//        log.info("Total Categories in DB: {}", categoryCount);
//        log.info("Total Products in DB: {}", productCount);
//        log.info("--------------- End Test ---------------");
//    }

    @Test
    @Transactional
    @Commit
    public void testInsertCategoriesProductsAndImages() {
        log.info("--------------- Start Test ---------------");

        // 1. ParentCategory Enum을 기반으로 카테고리 생성 및 저장
        for (ParentCategory parentCategory : ParentCategory.values()) {
            Category category = Category.builder()
                    .category(parentCategory)
                    .delFlag(false)
                    .build();
            categoryRepository.save(category);
            log.info("Inserted Category: {}", category);
        }

        // 2. 카테고리 조회
        List<Category> categories = categoryRepository.findAll();
        assertThat(categories.size()).isEqualTo(ParentCategory.values().length);

        // 3. 상품 100개 생성 및 저장 (각 상품에 랜덤한 이미지 추가)
        IntStream.rangeClosed(1, 100).forEach(i -> {
            // 상품 생성
            Product product = Product.builder()
                    .pname("상품 " + i)
                    .pdesc("설명 " + i)
                    .price(1000 * i)
                    .delFlag(false)
                    .build();

            // 랜덤한 이미지 수 (1~3개)
            int imageCount = random.nextInt(3) + 1;
            for (int j = 0; j < imageCount; j++) {
                Image image = new Image();
                image.setOrd(j);
                image.setFileName("image_" + i + "_" + j + ".jpg");
                image.setProduct(product); // 이미지에 상품 연결
                product.getImages().add(image); // 상품에 이미지 추가
            }

            productRepository.save(product);
            log.info("Inserted Product with Images: {}", product);
        });

        // 4. 검증: 상품 및 이미지가 제대로 저장되었는지 확인
        long productCount = productRepository.count();
        assertThat(productCount).isEqualTo(100);
