package com.tripwhiz.tripwhizuserback.product.repository;

import com.tripwhiz.tripwhizuserback.category.domain.Category;
import com.tripwhiz.tripwhizuserback.category.domain.ParentCategory;
import com.tripwhiz.tripwhizuserback.category.domain.SubCategory;
import com.tripwhiz.tripwhizuserback.product.domain.Product;
import com.tripwhiz.tripwhizuserback.product.domain.ProductTheme;
import com.tripwhiz.tripwhizuserback.product.domain.ThemeCategory;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Commit;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@DataJpaTest
@Log4j2
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductRepoTests {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @Transactional
    @Commit
    public void createProducts() {
        // 카테고리 및 테마 데이터 설정
        List<Category> categories = createCategories();
        List<SubCategory> subCategories = createSubCategories(categories);
        List<ThemeCategory> themeCategories = createThemeCategories();
        Random random = new Random();

        // 상품 100개 생성
        IntStream.rangeClosed(1, 100).forEach(i -> {
            // 랜덤 카테고리 및 하위 카테고리 선택
            Category category = categories.get(random.nextInt(categories.size()));
            List<SubCategory> relatedSubCategories = getSubCategoriesByCategory(subCategories, category.getCno());
            SubCategory subCategory = relatedSubCategories.get(random.nextInt(relatedSubCategories.size()));

            // 랜덤 테마 선택
            ThemeCategory themeCategory = themeCategories.get(random.nextInt(themeCategories.size()));

            // 상품 생성
            Product product = Product.builder()
                    .pname("Product " + i)  // 상품 이름
                    .price(1000 + random.nextInt(9000)) // 1000 ~ 9999 범위의 랜덤 가격
                    .pdesc("Description for product " + i) // 상품 설명
                    .category(category)
                    .subCategory(subCategory)
                    .build();

            // 상품 저장
            productRepository.save(product);

            // 상품 테마 설정
            ProductTheme productTheme = ProductTheme.builder()
                    .product(product)
                    .themeCategory(themeCategory)
                    .build();

            log.info("Product created: {}", product);
            log.info("ProductTheme created: {}", productTheme);
        });

        log.info("100개의 상품이 성공적으로 생성되었습니다.");
    }

    // 카테고리 데이터 생성 메서드
    private List<Category> createCategories() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(1L, ParentCategory.STORAGE, false));
        categories.add(new Category(2L, ParentCategory.CLOTHING, false));
        categories.add(new Category(3L, ParentCategory.SAFETY, false));
        categories.add(new Category(4L, ParentCategory.ACCESSORY, false));
        categories.add(new Category(5L, ParentCategory.ACTIVITY, false));
        return categories;
    }

    // 하위 카테고리 데이터 생성 메서드
    private List<SubCategory> createSubCategories(List<Category> categories) {
        List<SubCategory> subCategories = new ArrayList<>();

        subCategories.add(new SubCategory(1L, "파우치", categories.get(0)));
        subCategories.add(new SubCategory(2L, "케이스/커버", categories.get(0)));
        subCategories.add(new SubCategory(3L, "안대/목베개", categories.get(0)));
        subCategories.add(new SubCategory(4L, "와이파이 유심", categories.get(0)));
        subCategories.add(new SubCategory(5L, "아우터", categories.get(1)));
        subCategories.add(new SubCategory(6L, "상의/하의", categories.get(1)));
        subCategories.add(new SubCategory(7L, "잡화", categories.get(1)));
        subCategories.add(new SubCategory(8L, "뷰티케어", categories.get(2)));
        subCategories.add(new SubCategory(9L, "세면도구", categories.get(2)));
        subCategories.add(new SubCategory(10L, "상비약", categories.get(2)));
        subCategories.add(new SubCategory(11L, "전기/전자용품", categories.get(3)));
        subCategories.add(new SubCategory(12L, "여행 아이템", categories.get(3)));
        subCategories.add(new SubCategory(13L, "캠핑/등산", categories.get(4)));
        subCategories.add(new SubCategory(14L, "수영", categories.get(4)));
        subCategories.add(new SubCategory(15L, "야외/트래킹", categories.get(4)));
        return subCategories;
    }

    // 테마 카테고리 데이터 생성 메서드
    private List<ThemeCategory> createThemeCategories() {
        List<ThemeCategory> themeCategories = new ArrayList<>();
        themeCategories.add(new ThemeCategory(1L, "Healing", false));
        themeCategories.add(new ThemeCategory(2L, "Business", false));
        themeCategories.add(new ThemeCategory(3L, "Eating", false));
        themeCategories.add(new ThemeCategory(4L, "Shopping", false));
        themeCategories.add(new ThemeCategory(5L, "Activity", false));
        themeCategories.add(new ThemeCategory(6L, "Culture", false));
        return themeCategories;
    }

    // 카테고리 ID로 하위 카테고리 필터링
    private List<SubCategory> getSubCategoriesByCategory(List<SubCategory> subCategories, Long cno) {
        List<SubCategory> filtered = new ArrayList<>();
        for (SubCategory subCategory : subCategories) {
            if (subCategory.getCategory().getCno().equals(cno)) {
                filtered.add(subCategory);
            }
        }
        return filtered;
    }
}