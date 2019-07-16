package com.zerofinance.aip;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * AIPApplication
 */
@SpringBootApplication
public class AipApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(AipApplication.class)
                .properties("spring.config.name=application,swagger,version").run(args);
    }
}