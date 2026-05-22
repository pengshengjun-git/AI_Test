package com.aitest.auth.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
    /**
     * 主方法，演示BCrypt密码编码器的使用
     * 用于演示密码的加密过程
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 创建BCrypt密码编码器实例
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        // 原始密码
        String rawPassword = "admin123";
        // 对原始密码进行加密
        String hashedPassword = encoder.encode(rawPassword);
        // 输出原始密码
        System.out.println("Raw password: " + rawPassword);
        // 输出加密后的密码
        System.out.println("Hashed password: " + hashedPassword);
    }
}