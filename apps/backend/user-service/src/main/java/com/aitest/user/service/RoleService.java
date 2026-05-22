package com.aitest.user.service;

import com.aitest.common.exception.BusinessException;
import com.aitest.user.entity.Role;
import com.aitest.user.entity.UserRole;
import com.aitest.user.mapper.RoleMapper;
import com.aitest.user.mapper.UserRoleMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色服务
 */
@Service
public class RoleService extends ServiceImpl<RoleMapper, Role> {

    private final RoleMapper roleMapper;
    private final UserRoleMapper userRoleMapper;

    public RoleService(RoleMapper roleMapper, UserRoleMapper userRoleMapper) {
        this.roleMapper = roleMapper;
        this.userRoleMapper = userRoleMapper;
    }

    /**
     * 根据编码查询角色
     */
    public Role getByCode(String code) {
        return getOne(new LambdaQueryWrapper<Role>()
                .eq(Role::getCode, code)
                .eq(Role::getDeleted, 0));
    }

    /**
     * 创建角色
     */
    @Transactional(rollbackFor = Exception.class)
    public Role createRole(Role role) {
        Role existRole = getByCode(role.getCode());
        if (existRole != null) {
            throw new BusinessException("角色编码已存在");
        }
        role.setStatus(1);
        save(role);
        return role;
    }

    /**
     * 更新角色
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(Long roleId, Role role) {
        Role existRole = getById(roleId);
        if (existRole == null || existRole.getDeleted() == 1) {
            throw new BusinessException("角色不存在");
        }
        role.setId(roleId);
        updateById(role);
    }

    /**
     * 删除角色
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(Long roleId) {
        Role role = getById(roleId);
        if (role == null || role.getDeleted() == 1) {
            throw new BusinessException("角色不存在");
        }
        role.setDeleted(1);
        updateById(role);
    }

    /**
     * 为用户分配角色
     */
    @Transactional(rollbackFor = Exception.class)
    public void assignRoleToUser(Long userId, Long roleId) {
        // 检查是否已分配
        UserRole exist = userRoleMapper.selectOne(
                new LambdaQueryWrapper<UserRole>()
                        .eq(UserRole::getUserId, userId)
                        .eq(UserRole::getRoleId, roleId)
        );
        if (exist != null) {
            return;
        }
        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        userRole.setRoleId(roleId);
        userRoleMapper.insert(userRole);
    }

    /**
     * 移除用户角色
     */
    @Transactional(rollbackFor = Exception.class)
    public void removeRoleFromUser(Long userId, Long roleId) {
        userRoleMapper.delete(
                new LambdaQueryWrapper<UserRole>()
                        .eq(UserRole::getUserId, userId)
                        .eq(UserRole::getRoleId, roleId)
        );
    }

    /**
     * 获取用户的角色列表
     */
    public List<Role> getRolesByUserId(Long userId) {
        return roleMapper.selectRolesByUserId(userId);
    }

    /**
     * 获取所有角色
     */
    public List<Role> getAllRoles() {
        return list(new LambdaQueryWrapper<Role>()
                .eq(Role::getDeleted, 0)
                .orderByAsc(Role::getId));
    }
}
