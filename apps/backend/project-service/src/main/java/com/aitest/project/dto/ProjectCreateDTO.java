package com.aitest.project.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 创建项目请求DTO
 */
@Data
public class ProjectCreateDTO {

    /**
     * 项目编码
     */
    private String code;

    /**
     * 项目名称
     */
    @NotBlank(message = "项目名称不能为空")
    private String name;

    /**
     * 项目描述
     */
    private String description;

    /**
     * 项目状态: PLANNING-规划中, IN_PROGRESS-进行中, COMPLETED-已完成, ARCHIVED-已归档
     */
    private String status;

    /**
     * 优先级: P0-P3
     */
    private String priority;
}
