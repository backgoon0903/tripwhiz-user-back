package com.tripwhiz.tripwhizuserback.cart.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@Log4j2
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CartRepoTests {

    @Autowired
    private CartRepository cartRepository;

    @Test
    public void cartTest() {

        log.info(cartRepository.findAll().toString());

    }

}
