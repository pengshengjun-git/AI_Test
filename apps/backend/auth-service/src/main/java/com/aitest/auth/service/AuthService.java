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

/**
 * 认证服务
 */
@Service
public class AuthService {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final UserMapper userMapper;
    
    public AuthService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * 用户登录
     */
    public LoginResponse login(LoginRequest request) {
        // 查询用户
        User user = userMapper.selectByUsername(request.getUsername());

        if (user == null) {
            throw new BusinessException(401, "用户名或密码错误");
        }

        // 验证密码（兼容明文密码用于测试）
        String storedPassword = user.getPasswordHash();
        boolean passwordValid = false;
        
        // 检查是否是BCrypt哈希（以$2a$或$2b$开头）
        if (storedPassword.startsWith("$2a$") || storedPassword.startsWith("$2b$")) {
            passwordValid = passwordEncoder.matches(request.getPassword(), storedPassword);
        } else {
            // 兼容明文密码（用于测试环境）
            passwordValid = request.getPassword().equals(storedPassword);
        }
        
        if (!passwordValid) {
            throw new BusinessException(401, "用户名或密码错误");
        }

        // 检查用户状态
        if (!"ACTIVE".equals(user.getStatus())) {
            throw new BusinessException(403, "账号已被禁用");
        }

        // 生成Token
        String token = JwtUtil.generateToken(user.getId(), user.getUsername());
        String refreshToken = JwtUtil.generateToken(user.getId(), user.getUsername());

        return new LoginResponse(token, refreshToken, user.getId(), user.getUsername(), user.getRealName());
    }

    /**
     * 用户注册
     */
    @Transactional(rollbackFor = Exception.class)
    public User register(RegisterRequest request) {
        // 检查用户名是否已存在
        User existUser = userMapper.selectByUsername(request.getUsername());
        if (existUser != null) {
            throw new BusinessException("用户名已存在");
        }

        // 检查邮箱是否已存在
        if (request.getEmail() != null) {
            User existEmailUser = userMapper.selectByEmail(request.getEmail());
            if (existEmailUser != null) {
                throw new BusinessException("邮箱已被使用");
            }
        }

        // 创建用户
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setRealName(request.getRealName());
        user.setStatus("ACTIVE");
        user.setMfaEnabled(0);

        userMapper.insert(user);

        // 自动分配访客角色
        assignDefaultRole(user.getId());

        return user;
    }

    /**
     * 分配默认角色
     */
    private void assignDefaultRole(Long userId) {
        // 这里简化处理，实际应该查询角色表获取访客角色ID并分配
    }

    /**
     * 验证Token并返回用户信息
     */
    public User validateToken(String token) {
        try {
            // 验证token并获取用户ID
            Long userId = JwtUtil.getUserIdFromToken(token);
            
            if (userId == null) {
                return null;
            }
            
            // 查询用户信息
            User user = userMapper.selectById(userId);
            
            if (user == null || user.getDeleted() == 1 || !"ACTIVE".equals(user.getStatus())) {
            return null;
        }
            
            return user;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 重置密码
     */
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(ResetPasswordRequest request) {
        // 验证用户名和邮箱是否匹配
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
}
