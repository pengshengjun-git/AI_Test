package com.aitest.auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * 认证服务启动类
 */
@SpringBootApplication(exclude = {
    RedisAutoConfiguration.class,
    RedisRepositoriesAutoConfiguration.class
})
@MapperScan("com.aitest.auth.mapper")
@ComponentScan(basePackages = {"com.aitest.auth", "com.aitest.common"})
public class AuthApplication {

/**
 * Spring Boot应用程序的主入口方法
 * 通过调用SpringApplication的run方法来启动应用程序
 *
 * @param args 命令行参数，用于在应用程序启动时传递配置信息
 */
    public static void main(String[] args) {
    // 调用SpringApplication的run方法启动应用程序
    // 第一个参数AuthApplication.class是应用程序的主配置类
    // 第二个参数args是启动时传入的命令行参数
        SpringApplication.run(AuthApplication.class, args);
    }
}
