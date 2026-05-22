package com.aitest.defect.dto;

import lombok.Data;

/**
 * 查询缺陷DTO
 */
@Data
public class DefectQueryDTO {
    private String title;
    private String priority;
    private String status;
    private Long projectId;
    private Long assignee;
    private Integer page = 1;
    private Integer size = 10;
}
