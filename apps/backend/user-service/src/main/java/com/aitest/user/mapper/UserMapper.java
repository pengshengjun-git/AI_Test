package com.aitest.user.mapper;

import com.aitest.user.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户Mapper
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据ID查询用户（使用明确的字段列表）
     */
    @Select("SELECT id, username, password_hash, email, role, status, created_at, updated_at, deleted FROM user WHERE id = #{id} AND deleted = 0")
    User selectUserById(@Param("id") Long id);

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
}
