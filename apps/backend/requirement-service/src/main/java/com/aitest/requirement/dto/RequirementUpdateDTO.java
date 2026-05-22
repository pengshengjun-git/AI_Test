package com.aitest.requirement.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 需求更新DTO
 */
@Data
public class RequirementUpdateDTO {

    /**
     * 需求ID
     */
    @NotNull(message = "需求ID不能为空")
    private Long id;

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
     * 更新人ID
     */
    private Long updatedBy;
}
