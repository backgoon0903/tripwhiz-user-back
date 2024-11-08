package com.tripwhiz.tripwhizuserback.category.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
@Log4j2
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CategoryRepoTests {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void testCategory() {

        log.info("------------------");

        log.info(categoryRepository.findByDno(1L));

    }


}
