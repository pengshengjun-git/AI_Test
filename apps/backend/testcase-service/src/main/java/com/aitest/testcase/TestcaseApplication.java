package com.aitest.testcase;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 测试用例服务启动类
 */
@SpringBootApplication(scanBasePackages = {"com.aitest.testcase", "com.aitest.common"})
@EnableDiscoveryClient
@MapperScan("com.aitest.testcase.mapper")
public class TestcaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestcaseApplication.class, args);
    }
}
