package com.github.caiyun;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Vik
 * @date 2025-08-28 17:21
 * @description
 */
@SpringBootApplication
@MapperScan("com.github.caiyun.mapper")
public class CaiYunApplication {
    public static void main(String[] args) {
        SpringApplication.run(CaiYunApplication.class, args);
    }
}

