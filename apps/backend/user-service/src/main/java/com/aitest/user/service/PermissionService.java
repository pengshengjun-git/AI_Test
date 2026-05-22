package com.aitest.user.service;

import com.aitest.common.exception.BusinessException;
import com.aitest.user.entity.Permission;
import com.aitest.user.entity.RolePermission;
import com.aitest.user.mapper.PermissionMapper;
import com.aitest.user.mapper.RolePermissionMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 权限服务
 */
@Service
public class PermissionService extends ServiceImpl<PermissionMapper, Permission> {

    private final PermissionMapper permissionMapper;
    private final RolePermissionMapper rolePermissionMapper;

    public PermissionService(PermissionMapper permissionMapper, RolePermissionMapper rolePermissionMapper) {
        this.permissionMapper = permissionMapper;
        this.rolePermissionMapper = rolePermissionMapper;
    }

    /**
     * 根据编码查询权限
     */
    public Permission getByCode(String code) {
        return getOne(new LambdaQueryWrapper<Permission>()
                .eq(Permission::getCode, code)
                .eq(Permission::getDeleted, 0));
    }

    /**
     * 创建权限
     */
    @Transactional(rollbackFor = Exception.class)
    public Permission createPermission(Permission permission) {
        Permission existPermission = getByCode(permission.getCode());
        if (existPermission != null) {
            throw new BusinessException("权限编码已存在");
        }
        save(permission);
        return permission;
    }

    /**
     * 更新权限
     */
    @Transactional(rollbackFor = Exception.class)
    public void updatePermission(Long permissionId, Permission permission) {
        Permission existPermission = getById(permissionId);
        if (existPermission == null || existPermission.getDeleted() == 1) {
            throw new BusinessException("权限不存在");
        }
        permission.setId(permissionId);
        updateById(permission);
    }

    /**
     * 删除权限
     */
    @Transactional(rollbackFor = Exception.class)
    public void deletePermission(Long permissionId) {
        Permission permission = getById(permissionId);
        if (permission == null || permission.getDeleted() == 1) {
            throw new BusinessException("权限不存在");
        }
        permission.setDeleted(1);
        updateById(permission);
    }

    /**
     * 为角色分配权限
     */
    @Transactional(rollbackFor = Exception.class)
    public void assignPermissionToRole(Long roleId, Long permissionId) {
        RolePermission exist = rolePermissionMapper.selectOne(
                new LambdaQueryWrapper<RolePermission>()
                        .eq(RolePermission::getRoleId, roleId)
                        .eq(RolePermission::getPermissionId, permissionId)
        );
        if (exist != null) {
            return;
        }
        RolePermission rolePermission = new RolePermission();
        rolePermission.setRoleId(roleId);
        rolePermission.setPermissionId(permissionId);
        rolePermissionMapper.insert(rolePermission);
    }

    /**
     * 移除角色权限
     */
    @Transactional(rollbackFor = Exception.class)
    public void removePermissionFromRole(Long roleId, Long permissionId) {
        rolePermissionMapper.delete(
                new LambdaQueryWrapper<RolePermission>()
                        .eq(RolePermission::getRoleId, roleId)
                        .eq(RolePermission::getPermissionId, permissionId)
        );
    }

    /**
     * 获取角色的权限列表
     */
    public List<Permission> getPermissionsByRoleId(Long roleId) {
        return permissionMapper.selectPermissionsByRoleId(roleId);
    }

    /**
     * 获取所有权限
     */
    public List<Permission> getAllPermissions() {
        return list(new LambdaQueryWrapper<Permission>()
                .eq(Permission::getDeleted, 0)
                .orderByAsc(Permission::getParentId)
                .orderByAsc(Permission::getSortOrder));
    }
}
