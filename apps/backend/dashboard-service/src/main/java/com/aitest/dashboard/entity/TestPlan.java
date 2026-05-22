package com.aitest.dashboard.entity;

import com.aitest.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("test_plan")
public class TestPlan extends BaseEntity {

    private String name;

    private String description;

    private Long projectId;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String status;

    private String owner;

    private Long createdBy;

    private Long updatedBy;
}
