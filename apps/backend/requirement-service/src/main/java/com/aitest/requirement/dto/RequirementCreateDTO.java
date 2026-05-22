package com.aitest.requirement.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 需求创建DTO
 */
@Data
public class RequirementCreateDTO {

    /**
     * 项目ID
     */
    @NotNull(message = "项目ID不能为空")
    private Long projectId;

    /**
     * 需求标题
     */
    @NotBlank(message = "需求标题不能为空")
    private String title;

    /**
     * 需求描述
     */
    private String description;

    /**
     * 需求类型
     */
    private String type;

    /**
     * 优先级
     */
    private String priority;

    /**
     * 状态
     */
    private String status;

    /**
     * 需求来源
     */
    private String source;

    /**
     * 创建人ID
     */
    private Long createdBy;
}
