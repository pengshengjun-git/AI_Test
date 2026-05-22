package com.aitest.project.dto;

import com.aitest.project.entity.Project;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 项目响应DTO - 用于向前端返回数据，包含状态值转换
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponseDTO {

    private Long id;
    private String name;
    private String code;
    private String description;
    private String status;
    private String priority;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer testcaseCount;
    private Integer defectCount;
    private Integer requirementCount;
    private Long createdBy;
    private String ownerName;

    /**
     * 从实体转换为响应DTO，包含状态值映射
     * 数据库状态: active, archived
     * 前端状态: PLANNING, IN_PROGRESS, COMPLETED, ARCHIVED
     */
    public static ProjectResponseDTO fromEntity(Project project) {
        ProjectResponseDTO dto = new ProjectResponseDTO();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setCode(project.getCode());
        dto.setDescription(project.getDescription());
        
        // 状态值映射
        String dbStatus = project.getStatus();
        if ("archived".equals(dbStatus) || "ARCHIVED".equals(dbStatus)) {
            dto.setStatus("ARCHIVED");
        } else if ("active".equals(dbStatus) || "ACTIVE".equals(dbStatus)) {
            dto.setStatus("IN_PROGRESS");
        } else {
            dto.setStatus(dbStatus != null ? dbStatus : "PLANNING");
        }
        
        dto.setPriority(project.getPriority());
        dto.setCreateTime(project.getCreateTime());
        dto.setUpdateTime(project.getUpdateTime());
        dto.setTestcaseCount(project.getTestcaseCount());
        dto.setDefectCount(project.getDefectCount());
        dto.setRequirementCount(project.getRequirementCount());
        dto.setCreatedBy(project.getCreatedBy());
        dto.setOwnerName(project.getOwnerName());
        return dto;
    }
}
