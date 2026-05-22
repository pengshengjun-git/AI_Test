package com.aitest.dashboard;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 工作台服务启动类
 */
@SpringBootApplication(scanBasePackages = {"com.aitest.dashboard", "com.aitest.common"})
@MapperScan("com.aitest.dashboard.mapper")
public class DashboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(DashboardApplication.class, args);
    }
}
