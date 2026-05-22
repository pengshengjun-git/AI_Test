package com.aitest.project;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 项目服务启动类
 */
@SpringBootApplication(scanBasePackages = {"com.aitest.project", "com.aitest.common"})
@EnableDiscoveryClient
@MapperScan("com.aitest.project.mapper")
public class ProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectApplication.class, args);
    }
}
