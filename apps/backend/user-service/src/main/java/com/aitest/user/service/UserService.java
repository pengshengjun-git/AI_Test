package com.aitest.user.service;

import com.aitest.common.dto.UserVO;
import com.aitest.common.exception.BusinessException;
import com.aitest.user.dto.UserCreateDTO;
import com.aitest.user.dto.UserQueryDTO;
import com.aitest.user.dto.UserUpdateDTO;
import com.aitest.user.entity.User;
import com.aitest.user.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户服务
 */
@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * 根据用户名查询用户
     */
    public User getByUsername(String username) {
        return getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username)
                .eq(User::getDeleted, 0));
    }

    /**
     * 根据ID查询用户
     */
    public UserVO getUserById(Long userId) {
        User user = getById(userId);
        if (user == null || user.getDeleted() == 1) {
            throw new BusinessException("用户不存在");
        }
        return convertToVO(user);
    }

    /**
     * 分页查询用户
     */
    public IPage<UserVO> queryUsers(UserQueryDTO dto) {
        Page<User> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        LambdaQueryWrapper<User> wrapper = buildQueryWrapper(dto);
        
        IPage<User> resultPage = page(page, wrapper);
        
        return resultPage.convert(this::convertToVO);
    }

    /**
     * 构建查询条件
     */
    private LambdaQueryWrapper<User> buildQueryWrapper(UserQueryDTO dto) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .eq(User::getDeleted, 0);
        
        if (dto.getUsername() != null && !dto.getUsername().isEmpty()) {
            wrapper.like(User::getUsername, dto.getUsername());
        }
        if (dto.getEmail() != null && !dto.getEmail().isEmpty()) {
            wrapper.like(User::getEmail, dto.getEmail());
        }
        if (dto.getPhone() != null && !dto.getPhone().isEmpty()) {
            wrapper.like(User::getPhone, dto.getPhone());
        }
        if (dto.getStatus() != null) {
            wrapper.eq(User::getStatus, dto.getStatus());
        }
        if (dto.getDepartmentId() != null) {
            wrapper.eq(User::getDepartmentId, dto.getDepartmentId());
        }
        if (dto.getRealName() != null && !dto.getRealName().isEmpty()) {
            wrapper.like(User::getRealName, dto.getRealName());
        }
        
        wrapper.orderByDesc(User::getCreateTime);
        return wrapper;
    }

    /**
     * 创建用户
     */
    @Transactional(rollbackFor = Exception.class)
    public UserVO createUser(UserCreateDTO dto) {
        if (getByUsername(dto.getUsername()) != null) {
            throw new BusinessException("用户名已存在");
        }

        User user = new User();
        BeanUtils.copyProperties(dto, user);
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        user.setStatus(1);
        user.setDeleted(0);
        user.setMfaEnabled(0);
        save(user);

        return getUserById(user.getId());
    }

    /**
     * 更新用户
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(Long userId, UserUpdateDTO dto) {
        User existUser = getById(userId);
        if (existUser == null || existUser.getDeleted() == 1) {
            throw new BusinessException("用户不存在");
        }

        BeanUtils.copyProperties(dto, existUser);
        updateById(existUser);
    }

    /**
     * 删除用户（软删除）
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long userId) {
        User user = getById(userId);
        if (user == null || user.getDeleted() == 1) {
            throw new BusinessException("用户不存在");
        }
        user.setDeleted(1);
        updateById(user);
    }

    /**
     * 禁用用户
     */
    @Transactional(rollbackFor = Exception.class)
    public void disableUser(Long userId) {
        User user = getById(userId);
        if (user == null || user.getDeleted() == 1) {
            throw new BusinessException("用户不存在");
        }
        user.setStatus(0);
        updateById(user);
    }

    /**
     * 启用用户
     */
    @Transactional(rollbackFor = Exception.class)
    public void enableUser(Long userId) {
        User user = getById(userId);
        if (user == null || user.getDeleted() == 1) {
            throw new BusinessException("用户不存在");
        }
        user.setStatus(1);
        updateById(user);
    }

    /**
     * 重置密码
     */
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(Long userId, String newPassword) {
        User user = getById(userId);
        if (user == null || user.getDeleted() == 1) {
            throw new BusinessException("用户不存在");
        }
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        updateById(user);
    }

    /**
     * 批量禁用用户
     */
    @Transactional(rollbackFor = Exception.class)
    public void batchDisableUsers(List<Long> userIds) {
        for (Long userId : userIds) {
            disableUser(userId);
        }
    }

    /**
     * 批量启用用户
     */
    @Transactional(rollbackFor = Exception.class)
    public void batchEnableUsers(List<Long> userIds) {
        for (Long userId : userIds) {
            enableUser(userId);
        }
    }

    /**
     * 转换为VO
     */
    private UserVO convertToVO(User user) {
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        
        List<String> roleCodes = userMapper.selectRoleCodesByUserId(user.getId());
        vo.setRoles(roleCodes);
        
        if (!roleCodes.isEmpty()) {
            vo.setRole(String.join(",", roleCodes));
        }
        
        List<String> permissionCodes = userMapper.selectPermissionCodesByUserId(user.getId());
        vo.setPermissions(permissionCodes);
        
        return vo;
    }

    /**
     * 验证密码
     */
    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * 加密密码
     */
    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    /**
     * 数据库连通性探测（运维接口）
     */
    public boolean testDatabaseConnection() {
        try {
            long count = this.count();
            return count >= 0;
        } catch (Exception e) {
            return false;
        }
    }
}
