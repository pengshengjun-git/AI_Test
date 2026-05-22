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
    private String visibility;
    private Long ownerId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer testcaseCount;
    private Integer defectCount;

    /**
     * 从实体转换为响应DTO，包含状态值转换
     */
    public static ProjectResponseDTO fromEntity(Project project) {
        ProjectResponseDTO dto = new ProjectResponseDTO();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setCode(project.getCode());
        dto.setDescription(project.getDescription());
        dto.setStatus(mapStatusToFrontend(project.getStatus()));
        dto.setPriority(project.getPriority());
        dto.setVisibility(project.getVisibility());
        dto.setOwnerId(project.getOwnerId());
        dto.setCreateTime(project.getCreateTime());
        dto.setUpdateTime(project.getUpdateTime());
        return dto;
    }

    /**
     * 将后端状态值转换为前端期望的状态值
     * 后端状态: active, archived
     * 前端状态: PLANNING, IN_PROGRESS, COMPLETED, ARCHIVED
     */
    private static String mapStatusToFrontend(String backendStatus) {
        if (backendStatus == null) {
            return "PLANNING";
        }
        String lowerStatus = backendStatus.toLowerCase();
        switch (lowerStatus) {
            case "archived":
                return "ARCHIVED";
            case "active":
            default:
                return "IN_PROGRESS";
        }
    }
}
