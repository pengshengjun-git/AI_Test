package com.aitest.defect.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 创建缺陷DTO
 */
@Data
public class DefectCreateDTO {
    @NotBlank(message = "标题不能为空")
    private String title;
    
    private String description;
    
    private String priority;
    
    private String status;
    
    private String type;
    
    private String severity;
    
    private String module;
    
    private String steps;
    
    private String expectedResult;
    
    private String actualResult;
    
    private Long requirementId;
    
    @NotNull(message = "项目ID不能为空")
    private Long projectId;

    private Long reporterId;
    
    private Long assignee;
}
