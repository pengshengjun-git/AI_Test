package com.aitest.auth.mapper;

import com.aitest.auth.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 用户Mapper（Auth服务专用）
 */
@Mapper
public interface UserMapper {

    /**
     * 根据用户名查询用户
     * <p>
     * 此方法通过SQL查询语句从数据库中查找指定用户名的用户记录。
     * 查询条件包括用户名和已删除标记(deleted=0)，确保只返回未删除的用户记录。
     *
     * @param username 要查询的用户名
     * @return 返回匹配的用户对象，如果未找到则可能返回null
     */
    @Select("SELECT id, username, password_hash, email, phone, real_name, department_id, status, mfa_enabled, created_at, updated_at, deleted FROM user WHERE username = #{username} AND deleted = 0")
    User selectByUsername(@Param("username") String username);

    /**
     * 根据邮箱查询用户
     */
    @Select("SELECT id, username, password_hash, email, status, mfa_enabled, created_at, updated_at, deleted FROM user WHERE email = #{email} AND deleted = 0")
    User selectByEmail(@Param("email") String email);

    /**
     * 根据用户ID查询用户
     */
    @Select("SELECT id, username, password_hash, email, status, mfa_enabled, created_at, updated_at, deleted FROM user WHERE id = #{id} AND deleted = 0")
    User selectById(@Param("id") Long id);

    /**
     * 插入用户
     */
    @Insert("INSERT INTO user (username, password_hash, email, phone, real_name, status, mfa_enabled, created_at, updated_at) " +
            "VALUES (#{username}, #{passwordHash}, #{email}, #{phone}, #{realName}, #{status}, #{mfaEnabled}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(User user);

    /**
     * 根据用户ID查询角色列表
     */
    @Select("SELECT r.code FROM role r " +
            "INNER JOIN user_role ur ON r.id = ur.role_id " +
            "WHERE ur.user_id = #{userId} AND r.status = 1 AND r.deleted = 0")
    List<String> selectRoleCodesByUserId(@Param("userId") Long userId);

    /**
     * 根据用户ID查询权限列表
     */
    @Select("SELECT DISTINCT p.code FROM permission p " +
            "INNER JOIN role_permission rp ON p.id = rp.permission_id " +
            "INNER JOIN user_role ur ON rp.role_id = ur.role_id " +
            "WHERE ur.user_id = #{userId} AND p.deleted = 0")
    List<String> selectPermissionCodesByUserId(@Param("userId") Long userId);

    /**
     * 更新用户密码
     */
    @Update("UPDATE user SET password_hash = #{passwordHash}, updated_at = NOW() WHERE id = #{id}")
    void updatePassword(@Param("id") Long id, @Param("passwordHash") String passwordHash);
}
