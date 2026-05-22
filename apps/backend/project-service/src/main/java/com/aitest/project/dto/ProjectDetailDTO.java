package com.aitest.project.dto;

import com.aitest.project.entity.Project;
import com.aitest.project.entity.ProjectMember;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 项目详情DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDetailDTO {

    /**
     * 项目信息
     */
    private Project project;

    /**
     * 项目成员列表
     */
    private List<ProjectMember> members;
}
