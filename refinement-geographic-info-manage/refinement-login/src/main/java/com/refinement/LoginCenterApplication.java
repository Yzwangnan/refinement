package com.refinement;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages = "com.refinement.mapper")
public class LoginCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoginCenterApplication.class);
    }
}
