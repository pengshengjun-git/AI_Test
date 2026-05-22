package com.aitest.user.dto;

import lombok.Data;

/**
 * 部门更新请求DTO
 */
@Data
public class DepartmentUpdateDTO {

    private String name;
    private String code;
    private Long leaderId;
    private Integer sortOrder;
    private String description;
    private Integer status;
}
