package com.aitest.user.controller;

import com.aitest.common.result.Result;
import com.aitest.common.dto.UserVO;
import com.aitest.user.dto.UserCreateDTO;
import com.aitest.user.dto.UserQueryDTO;
import com.aitest.user.dto.UserUpdateDTO;
import com.aitest.user.service.UserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户管理控制器
 */
@RestController
@RequestMapping({"/api/v1/users", "/api/users"})
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 分页查询用户列表
     */
    @GetMapping
    public Result<Map<String, Object>> listUsers(UserQueryDTO query) {
        IPage<UserVO> page = userService.queryUsers(query);
        Map<String, Object> data = new HashMap<>();
        data.put("list", page.getRecords());
        data.put("total", page.getTotal());
        data.put("page", page.getCurrent());
        data.put("size", page.getSize());
        return Result.success(data);
    }

    /**
     * 获取用户详情
     */
    @GetMapping("/{id}")
    public Result<UserVO> getUser(@PathVariable Long id) {
        UserVO user = userService.getUserById(id);
        return Result.success(user);
    }

    /**
     * 创建用户
     */
    @PostMapping
    public Result<UserVO> createUser(@RequestBody UserCreateDTO dto) {
        UserVO user = userService.createUser(dto);
        return Result.success(user);
    }

    /**
     * 更新用户
     */
    @PutMapping("/{id}")
    public Result<Void> updateUser(@PathVariable Long id, @RequestBody UserUpdateDTO dto) {
        userService.updateUser(id, dto);
        return Result.success();
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return Result.success();
    }

    /**
     * 禁用用户
     */
    @PutMapping("/{id}/disable")
    public Result<Void> disableUser(@PathVariable Long id) {
        userService.disableUser(id);
        return Result.success();
    }

    /**
     * 启用用户
     */
    @PutMapping("/{id}/enable")
    public Result<Void> enableUser(@PathVariable Long id) {
        userService.enableUser(id);
        return Result.success();
    }

    /**
     * 重置密码
     */
    @PutMapping("/{id}/reset-password")
    public Result<Void> resetPassword(@PathVariable Long id, @RequestParam String newPassword) {
        userService.resetPassword(id, newPassword);
        return Result.success();
    }

    /**
     * 批量禁用用户
     */
    @PutMapping("/batch-disable")
    public Result<Void> batchDisableUsers(@RequestBody List<Long> userIds) {
        userService.batchDisableUsers(userIds);
        return Result.success();
    }

    /**
     * 批量启用用户
     */
    @PutMapping("/batch-enable")
    public Result<Void> batchEnableUsers(@RequestBody List<Long> userIds) {
        userService.batchEnableUsers(userIds);
        return Result.success();
    }
}
