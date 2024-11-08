package com.tripwhiz.tripwhizuserback.category.repository;


import com.tripwhiz.tripwhizuserback.category.domain.Category;
import com.tripwhiz.tripwhizuserback.category.domain.CategoryProduct;
import com.tripwhiz.tripwhizuserback.category.domain.ParentCategory;
import com.tripwhiz.tripwhizuserback.product.domain.Product;
import com.tripwhiz.tripwhizuserback.product.repository.ProductRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Commit;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Log4j2
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class test {

    @Autowired
    private ParentCategoryRepository parentCategoryRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryProductRepository categoryProductRepository;

    private ParentCategory parentCategory;
    private Product product;

    @BeforeEach
    void setUp() {
        // 부모 카테고리 생성
        parentCategory = parentCategoryRepository.save(
                ParentCategory.builder()
                        .cname("Default Parent Category")
                        .theme("Default Theme")
                        .delFlag(false)
                        .build()
        );

        // 제품 생성
        product = productRepository.save(
                Product.builder()
                        .pname("Sample Product")
                        .price(100)
                        .build()
        );
    }

    @Test
    @DisplayName("카테고리와 제품을 연결하여 저장 테스트")
    @Commit
    public void createAndSaveCategoriesAndProducts() {
        // 카테고리 3개 생성 및 저장
        List<Category> categories = IntStream.rangeClosed(1, 3)
                .mapToObj(i -> Category.builder()
                        .dname("Category " + i)
                        .delFlag(false)
                        .parentCategory(parentCategory) // 부모 카테고리 설정
                        .build())
                .map(categoryRepository::save)
                .toList();

        // 각 카테고리와 제품을 CategoryProduct를 통해 연결하여 저장
        categories.forEach(category -> {
            CategoryProduct categoryProduct = CategoryProduct.builder()
                    .category(category)
                    .product(product)
                    .build();
            categoryProductRepository.save(categoryProduct);
        });

        // 부모 카테고리 개수 검증
        long parentCount = parentCategoryRepository.count();
        assertThat(parentCount).isEqualTo(1);

        // 카테고리 개수 검증
        long categoryCount = categoryRepository.count();
        assertThat(categoryCount).isEqualTo(3);

        // 제품 개수 검증
        long productCount = productRepository.count();
        assertThat(productCount).isEqualTo(1);

        // CategoryProduct 개수 검증
        long categoryProductCount = categoryProductRepository.count();
        assertThat(categoryProductCount).isEqualTo(3);
    }
}
