package com.aitest.defect;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 缺陷服务启动类
 */
@SpringBootApplication(scanBasePackages = {"com.aitest.defect", "com.aitest.common"})
@EnableDiscoveryClient
@MapperScan("com.aitest.defect.mapper")
public class DefectApplication {

    public static void main(String[] args) {
        SpringApplication.run(DefectApplication.class, args);
    }
}
