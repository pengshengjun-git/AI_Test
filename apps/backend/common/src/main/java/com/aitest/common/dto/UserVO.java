package com.aitest.common.dto;

import java.util.List;

public class UserVO {
    private Long id;
    private String username;
    private String realName;
    private String email;
    private String phone;
    private Integer status;
    private String role;
    private java.time.LocalDateTime createTime;
    private java.time.LocalDateTime updateTime;
    private List<String> roles;
    private List<String> permissions;

    // Getters
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getRealName() { return realName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public Integer getStatus() { return status; }
    public String getRole() { return role; }
    public java.time.LocalDateTime getCreateTime() { return createTime; }
    public java.time.LocalDateTime getUpdateTime() { return updateTime; }
    public List<String> getRoles() { return roles; }
    public List<String> getPermissions() { return permissions; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setRealName(String realName) { this.realName = realName; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setStatus(Integer status) { this.status = status; }
    public void setRole(String role) { this.role = role; }
    public void setCreateTime(java.time.LocalDateTime createTime) { this.createTime = createTime; }
    public void setUpdateTime(java.time.LocalDateTime updateTime) { this.updateTime = updateTime; }
    public void setRoles(List<String> roles) { this.roles = roles; }
    public void setPermissions(List<String> permissions) { this.permissions = permissions; }
}