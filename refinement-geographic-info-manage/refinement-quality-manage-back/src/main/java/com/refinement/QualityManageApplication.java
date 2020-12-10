package com.refinement;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@MapperScan(basePackages = "com.refinement.mapper")
@SpringBootApplication
@EnableDiscoveryClient
public class QualityManageApplication {

    public static void main(String[] args) {
        SpringApplication.run(QualityManageApplication.class);
    }
}
