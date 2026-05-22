package com.aitest.user.controller;

import com.aitest.common.context.UserContext;
import com.aitest.common.dto.UserVO;
import com.aitest.common.result.Result;
import com.aitest.user.entity.User;
import com.aitest.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/me")
    public Result<UserVO> getCurrentUser() {
        Long userId = UserContext.getUserId();
        UserVO user = userService.getUserById(userId);
        return Result.success(user);
    }

    /**
     * 根据ID获取用户信息
     */
    @GetMapping("/{userId}")
    public Result<UserVO> getUserById(@PathVariable Long userId) {
        UserVO user = userService.getUserById(userId);
        return Result.success(user);
    }

    /**
     * 创建用户
     */
    @PostMapping
    public Result<User> createUser(@Valid @RequestBody User user) {
        User createdUser = userService.createUser(user);
        return Result.success(createdUser);
    }

    /**
     * 更新用户
     */
    @PutMapping("/{userId}")
    public Result<Void> updateUser(@PathVariable Long userId, @RequestBody User user) {
        userService.updateUser(userId, user);
        return Result.success();
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{userId}")
    public Result<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return Result.success();
    }
}
