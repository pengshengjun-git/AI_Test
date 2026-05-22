package com.aitest.user.dto;

import lombok.Data;

import java.util.List;

/**
 * 用户创建请求DTO
 */
@Data
public class UserCreateDTO {

    private String username;
    private String password;
    private String email;
    private String phone;
    private String realName;
    private Long departmentId;
    private List<Long> roleIds;
}
