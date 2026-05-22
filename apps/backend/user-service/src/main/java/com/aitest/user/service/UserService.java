package com.aitest.user.service;

import com.aitest.common.dto.UserVO;
import com.aitest.common.exception.BusinessException;
import com.aitest.user.entity.User;
import com.aitest.user.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;

/**
 * 用户服务
 */
@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final UserMapper userMapper;
    private final RoleService roleService;
    private final StringRedisTemplate stringRedisTemplate;

    public UserService(UserMapper userMapper, RoleService roleService, StringRedisTemplate stringRedisTemplate) {
        this.userMapper = userMapper;
        this.roleService = roleService;
        this.stringRedisTemplate = stringRedisTemplate;
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
        User user = userMapper.selectUserById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return convertToVO(user);
    }

    /**
     * 创建用户
     */
    @Transactional(rollbackFor = Exception.class)
    public User createUser(User user) {
        // 检查用户名是否已存在
        User existUser = getByUsername(user.getUsername());
        if (existUser != null) {
            throw new BusinessException("用户名已存在");
        }
        // 加密密码
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        user.setStatus("ACTIVE");
        user.setMfaEnabled(0);
        save(user);
        return user;
    }

    /**
     * 更新用户
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(Long userId, User user) {
        User existUser = getById(userId);
        if (existUser == null || existUser.getDeleted() == 1) {
            throw new BusinessException("用户不存在");
        }
        user.setId(userId);
        // 不更新密码
        user.setPasswordHash(null);
        updateById(user);
    }

    /**
     * 删除用户
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
     * 转换为VO
     */
    private UserVO convertToVO(User user) {
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        // 设置角色和权限
        vo.setRoles(userMapper.selectRoleCodesByUserId(user.getId()));
        vo.setPermissions(userMapper.selectPermissionCodesByUserId(user.getId()));
        return vo;
    }

    /**
     * 获取用户角色列表
     */
    public List<String> getRoleCodes(Long userId) {
        return userMapper.selectRoleCodesByUserId(userId);
    }

    /**
     * 获取用户权限列表
     */
    public List<String> getPermissionCodes(Long userId) {
        return userMapper.selectPermissionCodesByUserId(userId);
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

    /**
     * Redis 连通性探测（运维接口）
     */
    public boolean testRedisConnection() {
        try {
            String key = "test:connection";
            stringRedisTemplate.opsForValue().set(key, "ok", Duration.ofSeconds(30));
            return "ok".equals(stringRedisTemplate.opsForValue().get(key));
        } catch (Exception e) {
            return false;
        }
    }
}
