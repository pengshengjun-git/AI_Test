package com.aitest.user.service;

import com.aitest.common.exception.BusinessException;
import com.aitest.user.dto.RoleCreateDTO;
import com.aitest.user.dto.RoleQueryDTO;
import com.aitest.user.dto.RoleUpdateDTO;
import com.aitest.user.dto.RoleVO;
import com.aitest.user.entity.Role;
import com.aitest.user.mapper.RoleMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色服务
 */
@Service
public class RoleService extends ServiceImpl<RoleMapper, Role> {

    private final RoleMapper roleMapper;

    public RoleService(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
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
     * 分页查询角色
     */
    public IPage<RoleVO> queryRoles(RoleQueryDTO dto) {
        Page<Role> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<Role>()
                .eq(Role::getDeleted, 0);
        
        if (dto.getName() != null && !dto.getName().isEmpty()) {
            wrapper.like(Role::getName, dto.getName());
        }
        if (dto.getCode() != null && !dto.getCode().isEmpty()) {
            wrapper.like(Role::getCode, dto.getCode());
        }
        if (dto.getStatus() != null) {
            wrapper.eq(Role::getStatus, dto.getStatus());
        }
        
        wrapper.orderByAsc(Role::getId);
        
        IPage<Role> resultPage = page(page, wrapper);
        
        return resultPage.convert(this::convertToVO);
    }

    /**
     * 创建角色
     */
    @Transactional(rollbackFor = Exception.class)
    public RoleVO createRole(RoleCreateDTO dto) {
        Role existRole = getByCode(dto.getCode());
        if (existRole != null) {
            throw new BusinessException("角色编码已存在");
        }

        Role role = new Role();
        BeanUtils.copyProperties(dto, role);
        role.setStatus(1);
        role.setDeleted(0);
        save(role);

        return getRoleById(role.getId());
    }

    /**
     * 更新角色
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(Long roleId, RoleUpdateDTO dto) {
        Role existRole = getById(roleId);
        if (existRole == null || existRole.getDeleted() == 1) {
            throw new BusinessException("角色不存在");
        }

        BeanUtils.copyProperties(dto, existRole);
        updateById(existRole);
    }

    /**
     * 删除角色（软删除）
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(Long roleId) {
        Role role = getById(roleId);
        if (role == null || role.getDeleted() == 1) {
            throw new BusinessException("角色不存在");
        }

        if ("ADMIN".equals(role.getCode())) {
            throw new BusinessException("系统管理员角色不可删除");
        }

        role.setDeleted(1);
        updateById(role);
    }

    /**
     * 获取角色详情
     */
    public RoleVO getRoleById(Long roleId) {
        Role role = getById(roleId);
        if (role == null || role.getDeleted() == 1) {
            throw new BusinessException("角色不存在");
        }
        return convertToVO(role);
    }

    /**
     * 获取所有角色
     */
    public List<RoleVO> getAllRoles() {
        List<Role> roles = list(new LambdaQueryWrapper<Role>()
                .eq(Role::getDeleted, 0)
                .orderByAsc(Role::getId));
        return roles.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    /**
     * 转换为VO
     */
    private RoleVO convertToVO(Role role) {
        RoleVO vo = new RoleVO();
        BeanUtils.copyProperties(role, vo);
        vo.setMenuIds(new ArrayList<>());
        return vo;
    }
}
