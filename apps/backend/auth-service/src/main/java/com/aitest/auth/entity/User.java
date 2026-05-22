package com.aitest.auth.entity;

import com.aitest.common.entity.BaseEntity;

/**
 * 用户实体（Auth服务专用）
 */
public class User extends BaseEntity {

    private String username;
    private String passwordHash;
    private String email;
    private String phone;
    private String realName;
    private String avatar;
    private String role;
    private String status;
    private Integer mfaEnabled;
    private Long departmentId;
    private Integer deleted;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getMfaEnabled() {
        return mfaEnabled;
    }

    public void setMfaEnabled(Integer mfaEnabled) {
        this.mfaEnabled = mfaEnabled;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }
}
