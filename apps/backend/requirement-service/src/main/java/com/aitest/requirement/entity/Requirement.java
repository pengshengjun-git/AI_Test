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
     * 需求编号
     */
    private String code;

    /**
     * 需求标题
     */
    private String title;

    /**
     * 需求描述
     */
    private String description;

    /**
     * 需求类型: feature-功能, bug-缺陷, improvement-改进
     */
    private String type;

    /**
     * 优先级: P0, P1, P2, P3
     */
    private String priority;

    /**
     * 状态: draft-草稿, pending-待审核, approved-已通过, rejected-已拒绝, implemented-已实现, testing-测试中, closed-已关闭
     */
    private String status;

    /**
     * 创建人ID
     */
    @TableField("created_by")
    private Long creatorId;

    /**
     * 负责人ID
     */
    private Long assigneeId;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 来源: manual-手动, ai-AI生成
     */
    private String source;

    /**
     * 是否AI分析过: 0-否, 1-是
     */
    private Integer aiAnalyzed;

    /**
     * 文档URL
     */
    private String documentUrl;
}
