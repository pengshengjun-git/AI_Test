package com.aitest.requirement.entity;

import com.aitest.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 需求实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("requirement")
public class Requirement extends BaseEntity {

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 需求标题
     */
    private String title;

    /**
     * 需求标题（兼容前端name字段）
     */
    @TableField(exist = false)
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
     * 需求来源: internal-内部需求, customer-客户需求, market-市场调研, tech-技术改进
     */
    private String source;

    /**
     * 提出人
     */
    private String proposer;

    /**
     * 提出时间
     */
    private String proposerTime;

    /**
     * 生效版本
     */
    private String effectiveVersion;

    /**
     * 验收标准
     */
    private String acceptanceCriteria;

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
     * 评审结果
     */
    private String reviewResult;

    /**
     * 评审意见
     */
    private String reviewComments;

    /**
     * 上线时间
     */
    private String onlineTime;

    /**
     * 关闭原因
     */
    private String closeReason;

    /**
     * 关联文档URL
     */
    private String documentUrl;

    /**
     * 是否AI解析过: 0-否, 1-是
     */
    private Integer aiAnalyzed;

    /**
     * 创建人ID
     */
    private Long createdBy;

    /**
     * 更新人ID
     */
    private Long updatedBy;

    /**
     * 创建人用户名（非数据库字段）
     */
    @TableField(exist = false)
    private String creatorName;

    /**
     * 项目名称（非数据库字段）
     */
    @TableField(exist = false)
    private String projectName;
}
