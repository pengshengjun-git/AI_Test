package com.aitest.project.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * 更新项目请求DTO
 */
@Data
public class ProjectUpdateDTO {

    /**
     * 项目名称
     */
    private String name;

    /**
     * 项目描述
     */
    private String description;

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
