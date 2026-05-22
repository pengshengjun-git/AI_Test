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
 * 提供用户认证相关的API接口，包括登录、注册、登出、token验证等功能
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * 认证控制器的构造函数
     * 使用依赖注入方式获取AuthService实例
     *
     * @param authService 认证服务对象，用于处理用户认证相关业务逻辑
     */
    public AuthController(AuthService authService) {
        // 通过构造函数注入认证服务对象
        this.authService = authService;
    }

    /**
     * 用户登录接口
     * 处理用户登录请求，验证用户名和密码
     *
     * @param request 登录请求对象，包含用户名和密码
     * @return 返回登录响应对象，包含token和用户信息
     */
    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return Result.success(response);
    }

    /**
     * 用户注册接口
     * 该接口用于处理新用户的注册请求，接收用户注册信息并返回注册成功的用户数据（不含密码）
     *
     * @param request 注册请求对象，包含用户注册信息
     * @return 返回注册成功的用户信息（不含密码）
     */
    @PostMapping("/register")  // HTTP POST请求映射到/register路径
    public Result<User> register(@Valid @RequestBody RegisterRequest request) {
        User user = authService.register(request);  // 调用认证服务处理用户注册逻辑
        // 不返回密码，确保安全性
        user.setPasswordHash(null);
        return Result.success(user);  // 返回注册成功的用户信息
    }

    /**
     * 刷新Token接口
     * 用于刷新用户的认证token，延长登录有效期
     *
     * @param refreshToken 刷新token，通过请求头传递
     * @return 返回新的登录响应对象（简化实现，实际应该验证并生成新token）
     */
    @PostMapping("/refresh")
    public Result<LoginResponse> refreshToken(@RequestHeader("Authorization") String refreshToken) {
        // 简化实现，实际应该验证refreshToken并生成新的token
        return Result.success(null);
    }

    /**
     * 登出功能接口
 * 处理用户登出请求，清除用户登录状态
 * 使用PostMapping注解映射HTTP POST请求到/logout路径
 *
 * @return 返回操作结果，成功状态码为200，无具体返回数据
     */
    @PostMapping("/logout")
    public Result<Void> logout() {
        // 简化实现，实际应该将token加入黑名单
    // 在实际应用中，这里应该将用户的token加入黑名单，
    // 防止token被再次使用，提高系统安全性
        return Result.success();
    }

    /**
     * 验证Token接口
     * 验证请求头中的token是否有效，并返回对应的用户信息
     *
     * @param authorization 认证token，通过请求头传递，格式为"Bearer {token}"
     * @return 返回用户信息或错误信息（token无效时）
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
     * 重置密码接口
 * 该接口用于处理用户重置密码的请求
 * 使用POST方法，请求路径为/reset-password
 *
 * @param request 重置密码的请求参数，包含必要的信息如用户名、新密码等
 * @return 返回操作结果，成功时返回success，失败时返回相应的错误信息
     */
    @PostMapping("/reset-password")
    public Result<Void> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
    // 调用authService的resetPassword方法处理密码重置逻辑
        authService.resetPassword(request);
    // 返回操作成功的结果
        return Result.success();
    }

    /**
     * 根据用户ID获取用户信息接口
 * 这是一个REST API端点，用于通过HTTP GET请求获取指定ID的用户信息
     *
 * @param userId 用户ID，通过路径变量传递
 * @return 返回一个Result对象，包含用户信息或错误信息
     */
    @GetMapping("/users/{userId}")  // HTTP GET请求映射，用于处理获取用户信息的请求
    public Result<User> getUserById(@PathVariable Long userId) {  // 方法定义，接收路径变量userId
        User user = authService.getUserById(userId);  // 调用authService获取用户信息
        if (user != null) {  // 检查用户是否存在
            user.setPasswordHash(null);  // 出于安全考虑，清除密码哈希值
            return Result.success(user);  // 返回成功响应，包含用户信息
        }
        return Result.error("用户不存在");  // 返回错误响应，提示用户不存在
    }
}
