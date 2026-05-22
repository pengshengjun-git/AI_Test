package com.aitest.common.util;

import java.util.HashSet;
import java.util.Set;

/**
 * 权限上下文工具类
 * 使用ThreadLocal存储当前用户权限信息
 */
public class PermissionContext {

    private static final ThreadLocal<Long> userId = new ThreadLocal<>();
    private static final ThreadLocal<String> username = new ThreadLocal<>();
    private static final ThreadLocal<Set<String>> roles = ThreadLocal.withInitial(HashSet::new);
    private static final ThreadLocal<Set<String>> permissions = ThreadLocal.withInitial(HashSet::new);
    private static final ThreadLocal<Long> departmentId = new ThreadLocal<>();

    /**
     * 设置用户信息
     */
    public static void setUserInfo(Long userId, String username) {
        PermissionContext.userId.set(userId);
        PermissionContext.username.set(username);
    }

    /**
     * 设置用户信息（完整）
     */
    public static void setUserInfo(Long userId, String username, Set<String> roles, Set<String> permissions, Long departmentId) {
        PermissionContext.userId.set(userId);
        PermissionContext.username.set(username);
        PermissionContext.roles.set(roles != null ? roles : new HashSet<>());
        PermissionContext.permissions.set(permissions != null ? permissions : new HashSet<>());
        PermissionContext.departmentId.set(departmentId);
    }

    /**
     * 获取用户ID
     */
    public static Long getUserId() {
        return userId.get();
    }

    /**
     * 获取用户名
     */
    public static String getUsername() {
        return username.get();
    }

    /**
     * 获取用户角色集合
     */
    public static Set<String> getRoles() {
        return roles.get();
    }

    /**
     * 获取用户权限集合
     */
    public static Set<String> getPermissions() {
        return permissions.get();
    }

    /**
     * 获取部门ID
     */
    public static Long getDepartmentId() {
        return departmentId.get();
    }

    /**
     * 设置角色
     */
    public static void setRoles(Set<String> userRoles) {
        roles.set(userRoles != null ? userRoles : new HashSet<>());
    }

    /**
     * 设置权限
     */
    public static void setPermissions(Set<String> userPermissions) {
        permissions.set(userPermissions != null ? userPermissions : new HashSet<>());
    }

    /**
     * 设置部门ID
     */
    public static void setDepartmentId(Long deptId) {
        departmentId.set(deptId);
    }

    /**
     * 判断用户是否为管理员
     * 规则：具有ADMIN角色或system:*权限
     */
    public static boolean isAdmin() {
        Set<String> userRoles = roles.get();
        Set<String> userPermissions = permissions.get();

        if (userRoles != null && userRoles.contains("ADMIN")) {
            return true;
        }

        if (userPermissions != null) {
            for (String permission : userPermissions) {
                if (permission.equals("system:*") || permission.startsWith("system:*")) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 检查是否具有指定权限
     */
    public static boolean hasPermission(String permission) {
        if (permission == null || permission.isEmpty()) {
            return true;
        }

        Set<String> userPermissions = permissions.get();
        if (userPermissions == null || userPermissions.isEmpty()) {
            return false;
        }

        if (isAdmin()) {
            return true;
        }

        return userPermissions.contains(permission);
    }

    /**
     * 检查是否具有指定角色
     */
    public static boolean hasRole(String role) {
        if (role == null || role.isEmpty()) {
            return true;
        }

        Set<String> userRoles = roles.get();
        if (userRoles == null || userRoles.isEmpty()) {
            return false;
        }

        return userRoles.contains(role);
    }

    /**
     * 清除上下文（线程结束时必须调用）
     */
    public static void clear() {
        userId.remove();
        username.remove();
        roles.remove();
        permissions.remove();
        departmentId.remove();
    }
}
