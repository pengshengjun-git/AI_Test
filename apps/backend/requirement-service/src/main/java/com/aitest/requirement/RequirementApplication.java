package com.aitest.requirement;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 需求管理服务启动类
 */
@SpringBootApplication(scanBasePackages = {"com.aitest.requirement", "com.aitest.common"})
@EnableDiscoveryClient
@MapperScan("com.aitest.requirement.mapper")
public class RequirementApplication {

    public static void main(String[] args) {
        SpringApplication.run(RequirementApplication.class, args);
    }
}
