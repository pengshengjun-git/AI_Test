package com.aitest.project.service;

import com.aitest.project.dto.ProjectCreateDTO;
import com.aitest.project.dto.ProjectDetailDTO;
import com.aitest.project.dto.ProjectQueryDTO;
import com.aitest.project.dto.ProjectStatisticsDTO;
import com.aitest.project.dto.ProjectUpdateDTO;
import com.aitest.project.entity.Project;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 项目Service接口
 */
public interface ProjectService extends IService<Project> {

    /**
     * 分页查询项目列表
     */
    IPage<Project> getProjectPage(ProjectQueryDTO queryDTO);

    /**
     * 获取项目详情
     */
    ProjectDetailDTO getProjectDetail(Long projectId);

    /**
     * 创建项目
     */
    Project createProject(ProjectCreateDTO createDTO);

    /**
     * 更新项目
     */
    Project updateProject(Long projectId, ProjectUpdateDTO updateDTO);

    /**
     * 删除项目
     */
    boolean deleteProject(Long projectId);

    /**
     * 归档项目
     */
    boolean archiveProject(Long projectId);

    /**
     * 取消归档项目
     */
    boolean unarchiveProject(Long projectId);

    /**
     * 获取项目统计信息
     */
    ProjectStatisticsDTO getStatistics();

    /**
     * 检查项目编码是否存在
     */
    boolean checkCodeExists(String code, Long excludeId);
    
    /**
     * 检查项目名称是否存在
     * @param name 项目名称
     * @param excludeId 排除的项目ID（用于更新时排除自身）
     * @return 是否存在
     */
    boolean checkNameExists(String name, Long excludeId);
}
