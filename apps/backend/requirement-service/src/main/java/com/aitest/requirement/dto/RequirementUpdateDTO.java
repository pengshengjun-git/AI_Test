package com.aitest.requirement.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * 需求更新DTO
 */
@Data
public class RequirementUpdateDTO {

    /**
     * 需求ID
     */
    private Long id;

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
     * 需求类型: functional-功能, non_feature-非功能, bug_fix-Bug修复, tech_debt-技术债务
     */
    private String type;

    /**
     * 优先级: P0, P1, P2, P3
     */
    private String priority;

    /**
     * 状态: draft-草稿, pending-待评审, approved-已批准, in_progress-进行中, completed-已完成, rejected-已拒绝, closed-已关闭
     */
    private String status;

    /**
     * 更新人ID
     */
    private Long updatedBy;

    /**
     * 需求来源: internal-内部需求, customer-客户需求, market-市场调研, tech-技术改进
     */
    private String source;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 项目ID（兼容下划线）
     */
    private Long project_id;

    /**
     * 提出人
     */
    private String proposer;

    /**
     * 提出时间
     */
    private String proposerTime;

    /**
     * 提出时间（兼容下划线）
     */
    private String proposer_time;

    /**
     * 生效版本
     */
    private String effectiveVersion;

    /**
     * 生效版本（兼容下划线）
     */
    private String effective_version;

    /**
     * 验收标准
     */
    private String acceptanceCriteria;

    /**
     * 验收标准（兼容下划线）
     */
    private String acceptance_criteria;

    /**
     * 负责人
     */
    private String owner;

    /**
     * 审核人
     */
    private String reviewer;

    /**
     * 权限范围
     */
    private String permissionScope;

    /**
     * 权限范围（兼容下划线）
     */
    private String permission_scope;

    /**
     * 评审结果
     */
    private String reviewResult;

    /**
     * 评审结果（兼容下划线）
     */
    private String review_result;

    /**
     * 评审意见
     */
    private String reviewComments;

    /**
     * 评审意见（兼容下划线）
     */
    private String review_comments;

    /**
     * 上线时间
     */
    private String onlineTime;

    /**
     * 上线时间（兼容下划线）
     */
    private String online_time;

    /**
     * 关闭原因
     */
    private String closeReason;

    /**
     * 关闭原因（兼容下划线）
     */
    private String close_reason;

    /**
     * 关联文档URL
     */
    private String documentUrl;

    /**
     * 是否AI解析过: 0-否, 1-是
     */
    private Integer aiAnalyzed;
}
