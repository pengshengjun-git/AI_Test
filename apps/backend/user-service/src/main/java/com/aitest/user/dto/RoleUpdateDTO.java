package com.aitest.user.dto;

import lombok.Data;

import java.util.List;

/**
 * 角色更新请求DTO
 */
@Data
public class RoleUpdateDTO {

    private String name;
    private String description;
    private Integer status;
    private List<Long> menuIds;
}
