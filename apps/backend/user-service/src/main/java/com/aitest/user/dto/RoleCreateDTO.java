package com.aitest.user.dto;

import lombok.Data;

import java.util.List;

/**
 * 角色创建请求DTO
 */
@Data
public class RoleCreateDTO {

    private String name;
    private String code;
    private String description;
    private List<Long> menuIds;
}
