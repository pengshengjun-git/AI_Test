package com.aitest.defect.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 创建缺陷DTO
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
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
    
    @JsonProperty("requirement_id")
    private Long requirementId;
    
    @JsonProperty("project_id")
    private Long projectId;

    @JsonProperty("reporter_id")
    private Long reporterId;
    
    @JsonProperty("assignee_id")
    private Long assignee;
}
