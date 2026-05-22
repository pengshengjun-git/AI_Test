package com.aitest.dashboard.entity;

import com.aitest.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("coverage")
public class Coverage extends BaseEntity {

    private Long projectId;

    private Long coveredLines;

    private Long totalLines;

    private Double coverageRate;

    private LocalDate reportDate;
}
