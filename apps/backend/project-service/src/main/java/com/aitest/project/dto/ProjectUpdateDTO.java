package com.aitest.project.dto;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;

/**
 * 更新项目请求DTO
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectUpdateDTO {

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
     * 项目状态: PLANNING-规划中, IN_PROGRESS-进行中, COMPLETED-已完成, ARCHIVED-已归档
     */
    private String status;

    /**
     * 优先级: P0-P3
     */
    private String priority;

    /**
     * 项目负责人ID
     */
    private Long ownerId;

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
}
