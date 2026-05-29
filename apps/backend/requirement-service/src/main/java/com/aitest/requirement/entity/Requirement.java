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
}
