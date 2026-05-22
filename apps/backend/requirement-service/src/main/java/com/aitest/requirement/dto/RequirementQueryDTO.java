package com.aitest.requirement.dto;

import lombok.Data;

/**
 * 需求查询DTO
 */
@Data
public class RequirementQueryDTO {

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 需求标题关键词
     */
    private String keyword;

    /**
     * 需求类型
     */
    private String type;

    /**
     * 优先级
     */
    private String priority;

    /**
     * 状态
     */
    private String status;

    /**
     * 页码
     */
    private Integer pageNum = 1;

    /**
     * 每页大小
     */
    private Integer pageSize = 10;
}
