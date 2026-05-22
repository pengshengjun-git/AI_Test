package com.aitest.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 认证服务安全配置
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

/**
 * 配置Spring Security的安全过滤器链
 * @param http HttpSecurity对象，用于配置安全设置
 * @return 配置好的SecurityFilterChain实例
 * @throws Exception 可能抛出的异常
 */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        // 禁用CSRF保护，因为是无状态的REST API
            .csrf(AbstractHttpConfigurer::disable)
        // 设置会话管理为无状态模式，不创建会话
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        // 配置HTTP请求授权规则
            .authorizeHttpRequests(auth -> auth
            // 允许所有以"/api/v1/auth/"开头的请求无需认证
                .requestMatchers("/api/v1/auth/**").permitAll()
            // 允许"/health"路径的请求无需认证，通常用于健康检查
                .requestMatchers("/health").permitAll()
            // 所有其他请求都需要认证
                .anyRequest().authenticated()
            );
    // 构建并返回SecurityFilterChain实例
        return http.build();
    }
}