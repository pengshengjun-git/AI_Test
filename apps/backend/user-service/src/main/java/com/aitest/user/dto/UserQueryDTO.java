package com.aitest.user.dto;

import lombok.Data;

/**
 * 用户查询条件DTO
 */
@Data
public class UserQueryDTO {

    private String username;
    private String email;
    private String phone;
    private Integer status;
    private Long departmentId;
    private String realName;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
