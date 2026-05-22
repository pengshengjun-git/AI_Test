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
     * 需求名称（兼容前端）
     */
    private String name;

    /**
     * 需求描述
     */
    private String description;

    /**
     * 需求类型: functional-功能, performance-性能, security-安全, usability-易用性
     */
    private String type;

    /**
     * 优先级: P0, P1, P2, P3
     */
    private String priority;

    /**
     * 状态: draft-草稿, reviewing-评审中, approved-已批准, implemented-已实现, closed-已关闭
     */
    private String status;

    /**
     * 需求来源: manual-手动, import-导入, jira-Jira, confluence-Confluence
     */
    private String source;

    /**
     * 关联文档URL
     */
    private String documentUrl;

    /**
     * 是否AI解析过: 0-否, 1-是
     */
    private Integer aiAnalyzed;
}
