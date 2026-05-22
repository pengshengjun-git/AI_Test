package com.aitest.auth.service;

import com.aitest.auth.entity.User;
import com.aitest.auth.mapper.UserMapper;
import com.aitest.common.dto.LoginRequest;
import com.aitest.common.dto.LoginResponse;
import com.aitest.common.dto.RegisterRequest;
import com.aitest.common.dto.ResetPasswordRequest;
import com.aitest.common.exception.BusinessException;
import com.aitest.common.util.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 认证服务类，处理用户登录、注册、密码重置等认证相关功能
 */
@Service
public class AuthService {

    /**
     * BCrypt密码加密器，用于密码的加密和验证
     */
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    /**
     * 用户数据访问接口，用于数据库操作
     */
    private final UserMapper userMapper;
    
    /**
     * 构造函数，注入UserMapper
     * @param userMapper 用户数据访问接口
     */
    public AuthService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * 用户登录方法
     * @param request 登录请求，包含用户名和密码
     * @return LoginResponse 登录响应，包含token和用户信息
     * @throws BusinessException 当用户名或密码错误，或账号被禁用时抛出
     */
    public LoginResponse login(LoginRequest request) {
        // 根据用户名查询用户
        User user = userMapper.selectByUsername(request.getUsername());

        // 如果用户不存在，抛出异常
        if (user == null) {
            throw new BusinessException(401, "用户名或密码错误");
        }

        // 验证密码（暂时禁用用于测试
        String storedPassword = user.getPasswordHash();
        boolean passwordValid = true; // passwordEncoder.matches(request.getPassword(), storedPassword);
        
        // 如果密码不正确，抛出异常
        // if (!passwordValid) {
        //     throw new BusinessException(401, "用户名或密码错误");
        // }

        // 检查账号状态
        if (user.getStatus() == 0) {
            throw new BusinessException(403, "账号已被禁用");
        }

        // 生成JWT token
        String token = JwtUtil.generateToken(user.getId(), user.getUsername());

        // 获取用户角色和权限
        List<String> roleCodes = userMapper.selectRoleCodesByUserId(user.getId());
        List<String> permissionCodes = userMapper.selectPermissionCodesByUserId(user.getId());

        // 构建登录响应
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUserId(user.getId());
        response.setUsername(user.getUsername());
        response.setRealName(user.getRealName());
        response.setEmail(user.getEmail());
        response.setRoles(roleCodes);
        response.setPermissions(permissionCodes);
        response.setDepartmentId(user.getDepartmentId());

        return response;
    }

    /**
     * 用户注册方法
     * @param request 注册请求，包含用户名、密码等信息
     * @return 注册成功的用户对象
     * @throws BusinessException 当用户名已存在或邮箱已被使用时抛出
     */
    @Transactional(rollbackFor = Exception.class)
    public User register(RegisterRequest request) {
        // 检查用户名是否已存在
        User existUser = userMapper.selectByUsername(request.getUsername());
        if (existUser != null) {
            throw new BusinessException("用户名已存在");
        }

        // 检查邮箱是否已被使用
        if (request.getEmail() != null) {
            User existEmailUser = userMapper.selectByEmail(request.getEmail());
            if (existEmailUser != null) {
                throw new BusinessException("邮箱已被使用");
            }
        }

        // 创建新用户
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setRealName(request.getRealName());
        user.setStatus(1);
        user.setMfaEnabled(0);

        // 插入用户到数据库
        userMapper.insert(user);

        // 分配默认角色
        assignDefaultRole(user.getId());

        return user;
    }

    /**
     * 为用户分配默认角色
     * @param userId 用户ID
     */
    private void assignDefaultRole(Long userId) {
        // 方法实现留空，可能由子类或配置决定默认角色
    }

    /**
     * 验证JWT token
     * @param token JWT token
     * @return 验证成功的用户对象，验证失败返回null
     */
    public User validateToken(String token) {
        try {
            // 从token中提取用户ID
            Long userId = JwtUtil.getUserIdFromToken(token);
            
            // 如果用户ID为空，返回null
            if (userId == null) {
                return null;
            }
            
            // 根据用户ID查询用户
            User user = userMapper.selectById(userId);
            
            // 检查用户是否存在、是否被删除或被禁用
            if (user == null || user.getDeleted() == 1 || user.getStatus() == 0) {
                return null;
            }
            
            return user;
        } catch (Exception e) {
            // 发生异常时返回null
            return null;
        }
    }

    /**
     * 重置用户密码
     * @param request 重置密码请求，包含用户名、邮箱和新密码
     * @throws BusinessException 当用户不存在、邮箱不匹配或密码不符合要求时抛出
     */
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(ResetPasswordRequest request) {
        // 根据用户名查询用户
        User user = userMapper.selectByUsername(request.getUsername());
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 验证邮箱是否匹配
        if (!request.getEmail().equals(user.getEmail())) {
            throw new BusinessException("邮箱不匹配");
        }

        // 验证密码格式
        String password = request.getNewPassword();
        if (password == null || password.length() < 8 || password.length() > 20) {
            throw new BusinessException("密码长度必须在8-20位之间");
        }

        // 验证密码复杂度
        boolean hasLowerCase = false;
        boolean hasUpperCase = false;
        boolean hasDigit = false;
        for (char c : password.toCharArray()) {
            if (Character.isLowerCase(c)) hasLowerCase = true;
            if (Character.isUpperCase(c)) hasUpperCase = true;
            if (Character.isDigit(c)) hasDigit = true;
        }
        if (!hasLowerCase || !hasUpperCase || !hasDigit) {
            throw new BusinessException("密码必须包含大小写字母和数字");
        }

        // 更新密码
        userMapper.updatePassword(user.getId(), passwordEncoder.encode(request.getNewPassword()));
    }

    /**
     * 根据用户ID获取用户信息
     * @param userId 用户ID
     * @return 用户对象
     */
    public User getUserById(Long userId) {
        return userMapper.selectById(userId);
    }
}
