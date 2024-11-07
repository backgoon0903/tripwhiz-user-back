package com.tripwhiz.tripwhizuserback.product.repository;

import com.tripwhiz.tripwhizuserback.product.domain.Product;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Commit;

import java.util.stream.IntStream;

@DataJpaTest
@Log4j2
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductRepoTests {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @Commit
    public void createDummyData() {
        // 100개의 더미 데이터 생성
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Product product = Product.builder()
                    .pname("Product " + i)
                    .price(1000 + i * 10) // 가격 예시로 1000씩 증가
                    .pdesc("Description for product " + i)
                    .delFlag(false)
                    .build();
            productRepository.save(product);
            log.info("Created Product: {}", product);
        });
    }

    @Test
    public void testRead() {

        log.info(productRepository.read(15L));

    }


}
