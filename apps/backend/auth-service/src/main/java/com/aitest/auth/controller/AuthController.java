package com.aitest.auth.controller;

import com.aitest.auth.entity.User;
import com.aitest.auth.service.AuthService;
import com.aitest.common.dto.LoginRequest;
import com.aitest.common.dto.LoginResponse;
import com.aitest.common.dto.RegisterRequest;
import com.aitest.common.dto.ResetPasswordRequest;
import com.aitest.common.result.Result;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return Result.success(response);
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<User> register(@Valid @RequestBody RegisterRequest request) {
        User user = authService.register(request);
        // 不返回密码
        user.setPasswordHash(null);
        return Result.success(user);
    }

    /**
     * 刷新Token
     */
    @PostMapping("/refresh")
    public Result<LoginResponse> refreshToken(@RequestHeader("Authorization") String refreshToken) {
        // 简化实现，实际应该验证refreshToken并生成新的token
        return Result.success(null);
    }

    /**
     * 登出
     */
    @PostMapping("/logout")
    public Result<Void> logout() {
        // 简化实现，实际应该将token加入黑名单
        return Result.success();
    }

    /**
     * 验证Token
     */
    @GetMapping("/validate")
    public Result<User> validateToken(@RequestHeader("Authorization") String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            Result<User> result = new Result<>();
            result.setCode(401);
            result.setMessage("未授权");
            return result;
        }
        
        String token = authorization.substring(7);
        
        // 验证token并获取用户信息
        User user = authService.validateToken(token);
        
        if (user != null) {
            user.setPasswordHash(null);
            return Result.success(user);
        }
        
        Result<User> result = new Result<>();
        result.setCode(401);
        result.setMessage("无效的token");
        return result;
    }

    /**
     * 重置密码
     */
    @PostMapping("/reset-password")
    public Result<Void> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return Result.success();
    }
}
