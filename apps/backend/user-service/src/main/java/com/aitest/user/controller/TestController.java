package com.aitest.user.controller;

import com.aitest.common.result.Result;
import com.aitest.user.entity.User;
import com.aitest.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试控制器 - 用于测试数据库连接
 */
@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private UserService userService;

    /**
     * 测试数据库连接
     */
    @GetMapping("/database")
    public Result<Map<String, Object>> testDatabase() {
        Map<String, Object> result = new HashMap<>();
        boolean success = userService.testDatabaseConnection();
        result.put("success", success);
        result.put("message", success ? "数据库连接成功" : "数据库连接失败");
        return Result.success(result);
    }

    /**
     * 根据用户名查询用户（测试缓存）
     */
    @GetMapping("/user/{username}")
    public Result<User> getUserByUsername(@PathVariable String username) {
        User user = userService.getByUsername(username);
        if (user != null) {
            // 隐藏密码
            user.setPasswordHash(null);
            return Result.success(user);
        }
        Result<User> result = new Result<>();
        result.setCode(500);
        result.setMessage("用户不存在");
        return result;
    }

    /**
     * 获取所有用户
     */
    @GetMapping("/users")
    public Result<Object> getAllUsers() {
        return Result.success(userService.list());
    }
}
