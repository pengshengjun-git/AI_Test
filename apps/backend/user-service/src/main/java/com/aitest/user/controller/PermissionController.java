package com.aitest.user.controller;

import com.aitest.common.result.Result;
import com.aitest.user.entity.Permission;
import com.aitest.user.service.PermissionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 权限控制器
 */
@RestController
@RequestMapping("/api/v1/permissions")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    /**
     * 获取所有权限
     */
    @GetMapping
    public Result<List<Permission>> getAllPermissions() {
        List<Permission> permissions = permissionService.getAllPermissions();
        return Result.success(permissions);
    }

    /**
     * 根据ID获取权限
     */
    @GetMapping("/{permissionId}")
    public Result<Permission> getPermissionById(@PathVariable Long permissionId) {
        Permission permission = permissionService.getById(permissionId);
        return Result.success(permission);
    }

    /**
     * 创建权限
     */
    @PostMapping
    public Result<Permission> createPermission(@Valid @RequestBody Permission permission) {
        Permission createdPermission = permissionService.createPermission(permission);
        return Result.success(createdPermission);
    }

    /**
     * 更新权限
     */
    @PutMapping("/{permissionId}")
    public Result<Void> updatePermission(@PathVariable Long permissionId, @RequestBody Permission permission) {
        permissionService.updatePermission(permissionId, permission);
        return Result.success();
    }

    /**
     * 删除权限
     */
    @DeleteMapping("/{permissionId}")
    public Result<Void> deletePermission(@PathVariable Long permissionId) {
        permissionService.deletePermission(permissionId);
        return Result.success();
    }

    /**
     * 为角色分配权限
     */
    @PostMapping("/{permissionId}/roles/{roleId}")
    public Result<Void> assignPermissionToRole(@PathVariable Long permissionId, @PathVariable Long roleId) {
        permissionService.assignPermissionToRole(roleId, permissionId);
        return Result.success();
    }

    /**
     * 移除角色权限
     */
    @DeleteMapping("/{permissionId}/roles/{roleId}")
    public Result<Void> removePermissionFromRole(@PathVariable Long permissionId, @PathVariable Long roleId) {
        permissionService.removePermissionFromRole(roleId, permissionId);
        return Result.success();
    }

    /**
     * 获取角色的权限列表
     */
    @GetMapping("/roles/{roleId}")
    public Result<List<Permission>> getPermissionsByRoleId(@PathVariable Long roleId) {
        List<Permission> permissions = permissionService.getPermissionsByRoleId(roleId);
        return Result.success(permissions);
    }
}
