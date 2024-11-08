package com.tripwhiz.tripwhizuserback.category.repository;

import com.tripwhiz.tripwhizuserback.category.domain.Category;
import com.tripwhiz.tripwhizuserback.category.domain.ParentCategory;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Commit;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Log4j2
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CategoryRepoTests {

    @Autowired
    private ParentCategoryRepository parentCategoryRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private ParentCategory parentCategory;

    @BeforeEach
    void setUp() {
        // 이미 존재하는 부모 카테고리가 없으면 새로 생성하여 테스트용으로 사용
        Optional<ParentCategory> existingParent = parentCategoryRepository.findById(1L);
        parentCategory = existingParent.orElseGet(() ->
                parentCategoryRepository.save(
                        ParentCategory.builder()
                                .cname("Default Parent Category")
                                .theme("Default Theme")
                                .delFlag(false)
                                .build()));
    }

    @Test
    @DisplayName("5개의 ParentCategory 더미 데이터 생성 및 저장 테스트")
    @Commit
    public void createAndSaveParentCategories() {
        // 5개의 부모 카테고리 생성 및 저장
        List<ParentCategory> parentCategories = IntStream.rangeClosed(1, 5)
                .mapToObj(i -> ParentCategory.builder()
                        .cname("Parent Category " + i)
                        .theme("Default Theme " + i)
                        .delFlag(false)
                        .build())
                .map(parentCategoryRepository::save)
                .collect(Collectors.toList());

        // 저장된 부모 카테고리 개수 검증
        long count = parentCategoryRepository.count();
        assertThat(count).isGreaterThanOrEqualTo(5); // 최소 5개의 부모 카테고리가 저장되었는지 확인
    }

    @Test
    @DisplayName("10개의 Category 더미 데이터 생성 및 저장 테스트")
    @Commit
    public void createAndSaveCategories() {
        // 10개의 자식 카테고리 생성 및 저장
        IntStream.rangeClosed(1, 10).forEach(i -> {
            Category category = Category.builder()
                    .dname("Category " + i)
                    .delFlag(false)
                    .parentCategory(parentCategory) // 부모 카테고리 설정
                    .build();
            categoryRepository.save(category);
        });

        // 저장된 자식 카테고리 개수 검증
        long categoryCount = categoryRepository.count();
        assertThat(categoryCount).isGreaterThanOrEqualTo(10); // 최소 10개의 카테고리가 저장되었는지 확인
    }
}
