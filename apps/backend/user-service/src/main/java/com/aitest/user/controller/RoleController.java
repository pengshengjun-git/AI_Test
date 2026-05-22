package com.aitest.user.controller;

import com.aitest.common.result.Result;
import com.aitest.user.entity.Role;
import com.aitest.user.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色控制器
 */
@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * 获取所有角色
     */
    @GetMapping
    public Result<List<Role>> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        return Result.success(roles);
    }

    /**
     * 根据ID获取角色
     */
    @GetMapping("/{roleId}")
    public Result<Role> getRoleById(@PathVariable Long roleId) {
        Role role = roleService.getById(roleId);
        return Result.success(role);
    }

    /**
     * 创建角色
     */
    @PostMapping
    public Result<Role> createRole(@Valid @RequestBody Role role) {
        Role createdRole = roleService.createRole(role);
        return Result.success(createdRole);
    }

    /**
     * 更新角色
     */
    @PutMapping("/{roleId}")
    public Result<Void> updateRole(@PathVariable Long roleId, @RequestBody Role role) {
        roleService.updateRole(roleId, role);
        return Result.success();
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/{roleId}")
    public Result<Void> deleteRole(@PathVariable Long roleId) {
        roleService.deleteRole(roleId);
        return Result.success();
    }

    /**
     * 为用户分配角色
     */
    @PostMapping("/{roleId}/users/{userId}")
    public Result<Void> assignRoleToUser(@PathVariable Long roleId, @PathVariable Long userId) {
        roleService.assignRoleToUser(userId, roleId);
        return Result.success();
    }

    /**
     * 移除用户角色
     */
    @DeleteMapping("/{roleId}/users/{userId}")
    public Result<Void> removeRoleFromUser(@PathVariable Long roleId, @PathVariable Long userId) {
        roleService.removeRoleFromUser(userId, roleId);
        return Result.success();
    }

    /**
     * 获取用户的角色列表
     */
    @GetMapping("/users/{userId}")
    public Result<List<Role>> getRolesByUserId(@PathVariable Long userId) {
        List<Role> roles = roleService.getRolesByUserId(userId);
        return Result.success(roles);
    }
}
