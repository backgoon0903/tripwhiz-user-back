//package com.tripwhiz.tripwhizuserback.product.repository;
//
//import com.tripwhiz.tripwhizuserback.category.domain.Category;
//import com.tripwhiz.tripwhizuserback.category.domain.SubCategory;
//import com.tripwhiz.tripwhizuserback.product.domain.Product;
//import com.tripwhiz.tripwhizuserback.category.domain.ThemeCategory;
//import com.tripwhiz.tripwhizuserback.category.repository.CategoryRepository;
//import com.tripwhiz.tripwhizuserback.category.repository.SubCategoryRepository;
//import lombok.extern.log4j.Log4j2;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.annotation.Commit;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.Map;
//import java.util.stream.IntStream;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//
//@DataJpaTest
//@Log4j2
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//public class ProductRepoTests {
//
//    @Autowired
//    private CategoryRepository categoryRepository;
//
//    @Autowired
//    private SubCategoryRepository subCategoryRepository;
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    // 상위 카테고리와 하위 카테고리 정의
//    private final Map<String, List<String>> categoryMap = Map.of(
//            "Health", List.of("Pain Relief", "Bandages", "Masks", "Hand Sanitizer", "Mosquito Repellent"),
//            "Electronics", List.of("Charger", "Adapter", "Battery Pack", "Backup Battery", "Portable Charger"),
//            "Clothes", List.of("T-shirt", "Jacket", "Socks", "Hat", "Gloves"),
//            "Toiletries", List.of("Toothbrush", "Toothpaste", "Shampoo", "Soap", "Lotion"),
//            "Outdoors", List.of("Tent", "Sleeping Bag", "Backpack", "Flashlight", "Compass")
//    );
//
//    // 테마 목록 정의
//    private final List<ThemeCategory> themes = List.of(
//            ThemeCategory.RELAXATION, ThemeCategory.BUSINESS, ThemeCategory.GASTRONOMY,
//            ThemeCategory.SHOPPING, ThemeCategory.ACTIVITY, ThemeCategory.CULTURE
//    );
//
//    @Test
//    @Transactional
//    @Commit
//    public void setUp() {
//        // 각 테마마다 상위 카테고리 및 하위 카테고리를 생성하고, 상품을 등록
//        themes.forEach(this::createCategoriesAndProductsForTheme);
//    }
//
//    private void createCategoriesAndProductsForTheme(ThemeCategory theme) {
//        // 상위 카테고리 반복하여 하위 카테고리와 상품 생성
//        categoryMap.forEach((categoryName, subCategoryNames) -> {
//            // 상위 카테고리 생성 및 저장
//            final Category category = categoryRepository.save(Category.builder()
//                    .cname(categoryName)
//                    .themeCategory(theme) // 테마 설정
//                    .delFlag(false)
//                    .build());
//
//            // 하위 카테고리 생성 및 저장
//            subCategoryNames.forEach(subCategoryName -> {
//                final SubCategory subCategory = subCategoryRepository.save(SubCategory.builder()
//                        .sname(subCategoryName)
//                        .category(category) // 상위 카테고리 연결
//                        .build());
//
//                // 각 하위 카테고리마다 10개의 상품 생성 및 저장
//                IntStream.rangeClosed(1, 10).forEach(i -> {
//                    String productName = subCategoryName + " Product " + i;
//                    Product product = Product.builder()
//                            .pname(productName)
//                            .pdesc("Description for " + productName)
//                            .price(1000 * i)
//                            .fileUrl("https://example.com/images/" + productName + ".jpg") // HTTPS 링크로 변경
//                            .subCategory(subCategory) // 하위 카테고리 연결
//                            .category(category) // 상위 카테고리 연결
//                            .themeCategory(theme) // 테마 연결
//                            .build();
//                    productRepository.save(product);
//                });
//            });
//        });
//    }
//
//    @Test
//    @Transactional
//    public void testCategoryProductInsertion() {
//        // 총 카테고리 및 상품 개수 확인
//        long categoryCount = categoryRepository.count();
//        long subCategoryCount = subCategoryRepository.count();
//        long productCount = productRepository.count();
//
//        assertThat(categoryCount).isEqualTo(categoryMap.size() * themes.size()); // 6개의 테마 사용
//        assertThat(subCategoryCount).isEqualTo(categoryMap.values().stream().mapToInt(List::size).sum() * themes.size());
//        assertThat(productCount).isEqualTo(categoryMap.values().stream().mapToInt(List::size).sum() * themes.size() * 10);
//
//        log.info("Total Categories in DB: " + categoryCount);
//        log.info("Total SubCategories in DB: " + subCategoryCount);
//        log.info("Total Products in DB: " + productCount);
//    }
//}
