package com.tripwhiz.tripwhizuserback;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Log4j2
@SpringBootApplication
public class TripwhizUserBackApplication {

    public static void main(String[] args) {

        log.info("--------------------------");
        SpringApplication.run(TripwhizUserBackApplication.class, args);
        log.info("--------------------------");
    }

}
