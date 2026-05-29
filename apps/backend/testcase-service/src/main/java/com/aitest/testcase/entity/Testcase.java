package com.aitest.testcase.entity;

import com.aitest.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 测试用例实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("testcase")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Testcase extends BaseEntity {

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 关联需求ID
     */
    private Long requirementId;

    /**
     * 用例标题
     */
    private String title;

    /**
     * 用例描述
     */
    private String description;

    /**
     * 前置条件
     */
    private String preconditions;

    /**
     * 优先级: P0, P1, P2, P3
     */
    private String priority;

    /**
     * 用例类型: functional-功能, boundary-边界, exception-异常, security-安全, api-接口, performance-性能, compatibility-兼容性
     */
    private String type;

    /**
     * 状态: draft-草稿, reviewed-已评审, approved-已批准, deprecated-已废弃
     */
    private String status;

    /**
     * 执行状态: pending-未执行, running-执行中, passed-通过, failed-失败, blocked-阻塞, skipped-跳过
     */
    private String testStatus;

    /**
     * 测试模块
     */
    private String testModule;

    /**
     * 测试步骤
     */
    private String steps;

    /**
     * 预期结果
     */
    private String expectedResult;

    /**
     * 实际结果
     */
    private String actualResult;

    /**
     * 标签,逗号分隔（非数据库字段）
     */
    @TableField(exist = false)
    private String tags;

    /**
     * 是否AI生成: 0-否, 1-是
     */
    private Integer aiGenerated;

    /**
     * AI任务ID（非数据库字段）
     */
    @TableField(exist = false)
    private Long aiTaskId;

    /**
     * 风险评分 0-100（非数据库字段）
     */
    @TableField(exist = false)
    private BigDecimal riskScore;

    /**
     * 创建人ID
     */
    private Long createdBy;

    /**
     * 更新人ID
     */
    private Long updatedBy;

    /**
     * 项目名称（非数据库字段）
     */
    @TableField(exist = false)
    private String projectName;

    /**
     * 需求标题（非数据库字段）
     */
    @TableField(exist = false)
    private String requirementTitle;

    /**
     * 创建人用户名（非数据库字段）
     */
    @TableField(exist = false)
    private String creatorName;
}
