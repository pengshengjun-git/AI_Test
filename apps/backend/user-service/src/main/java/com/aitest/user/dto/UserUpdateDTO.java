package com.aitest.user.dto;

import lombok.Data;

import java.util.List;

/**
 * 用户更新请求DTO
 */
@Data
public class UserUpdateDTO {

    private String email;
    private String phone;
    private String realName;
    private Long departmentId;
    private List<Long> roleIds;
}
