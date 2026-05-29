package com.aitest.defect.entity;

import com.aitest.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 缺陷实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("defect")
public class Defect extends BaseEntity {

    private String title;

    private String description;

    private String priority;

    private String status;

    private String type;

    private String severity;

    private String module;

    private String stepsToReproduce;

    private String expectedResult;

    private String actualResult;

    private Long requirementId;

    private Long projectId;

    private Long reporterId;

    private Long assignee;

    private Long createdBy;

    private Long updatedBy;

    @TableField(exist = false)
    private String projectName;

    @TableField(exist = false)
    private String requirementName;

    @TableField(exist = false)
    private String handler;

    @TableField(exist = false)
    private String reporterName;
}
