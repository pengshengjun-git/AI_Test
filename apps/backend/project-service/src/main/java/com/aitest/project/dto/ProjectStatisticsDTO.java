package com.aitest.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目统计DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectStatisticsDTO {

    /**
     * 项目总数
     */
    private Long totalProjects;

    /**
     * 活跃项目数
     */
    private Long activeProjects;

    /**
     * 归档项目数
     */
    private Long archivedProjects;

    /**
     * 项目成员总数
     */
    private Long totalMembers;
}
