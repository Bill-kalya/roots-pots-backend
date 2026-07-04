package com.rootsandpots;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RootsAndPotsApplication {
    public static void main(String[] args) {
        SpringApplication.run(RootsAndPotsApplication.class, args);
    }
}
