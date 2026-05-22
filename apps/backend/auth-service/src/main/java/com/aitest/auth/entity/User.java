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
    private Integer status;
    private Integer mfaEnabled;
    private Long departmentId;
    private Integer deleted;

    /**
     * 获取用户名的方法
     *
     * @return 返回用户名字符串
     */
    public String getUsername() {
        return username; // 返回username属性的值
    }

    /**
     * 设置用户名的方法
     *
     * @param username 要设置的用户名
     */
    public void setUsername(String username) {
        // 将传入的username参数赋值给对象的username属性
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

    /**
     * 获取手机号码的方法
     *
     * @return 返回手机号码字符串
     */
    public String getPhone() {
        return phone; // 返回phone属性值
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
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
