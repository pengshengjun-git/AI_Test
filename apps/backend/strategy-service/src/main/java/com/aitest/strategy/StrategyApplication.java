package com.aitest.strategy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 策略服务启动类
 */
@SpringBootApplication(scanBasePackages = {"com.aitest.strategy", "com.aitest.common"})
@MapperScan("com.aitest.strategy.mapper")
public class StrategyApplication {

    public static void main(String[] args) {
        SpringApplication.run(StrategyApplication.class, args);
    }
}
