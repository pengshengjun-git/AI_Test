package com.aitest.user.dto;

import lombok.Data;

/**
 * 角色查询条件DTO
 */
@Data
public class RoleQueryDTO {

    private String name;
    private String code;
    private Integer status;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
