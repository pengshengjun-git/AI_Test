package com.aitest.project.entity;

import com.aitest.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 项目实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("project")
public class Project extends BaseEntity {

    /**
     * 项目名称
     */
    private String name;

    /**
     * 项目编码
     */
    private String code;

    /**
     * 项目描述
     */
    private String description;

    /**
     * 项目负责人ID
     */
    private Long ownerId;

    /**
     * 状态: active-活跃, archived-归档
     */
    private String status;

    /**
     * 优先级: P0-P3
     */
    private String priority;

    /**
     * 可见性: private-私有, public-公开
     */
    private String visibility;

    /**
     * 开始日期
     */
    private LocalDate startDate;

    /**
     * 结束日期
     */
    private LocalDate endDate;

    /**
     * 创建人ID
     */
    private Long createdBy;

    /**
     * 更新人ID
     */
    private Long updatedBy;
}
