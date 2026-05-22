package com.aitest.user.dto;

import lombok.Data;

/**
 * 部门创建请求DTO
 */
@Data
public class DepartmentCreateDTO {

    private Long parentId;
    private String name;
    private String code;
    private Long leaderId;
    private Integer sortOrder;
    private String description;
}
