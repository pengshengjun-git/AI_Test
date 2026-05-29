package com.aitest.project.entity;

import com.aitest.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 项目实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("project")
public class Project extends BaseEntity {

    /**
     * 项目编码
     */
    private String code;

    /**
     * 项目名称
     */
    private String name;

    /**
     * 项目描述
     */
    private String description;

    /**
     * 状态: PLANNING-规划中, IN_PROGRESS-进行中, COMPLETED-已完成, ARCHIVED-已归档
     */
    private String status;

    /**
     * 优先级: P0-P3
     */
    private String priority;

    /**
     * 用例数
     */
    @TableField(exist = false)
    private Integer testcaseCount;

    /**
     * 缺陷数
     */
    @TableField(exist = false)
    private Integer defectCount;

    /**
     * 需求数
     */
    @TableField(exist = false)
    private Integer requirementCount;

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
    private String ownerName;
}
