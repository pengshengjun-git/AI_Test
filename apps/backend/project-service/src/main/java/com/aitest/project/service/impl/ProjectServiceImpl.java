package com.aitest.project.service.impl;

import com.aitest.project.dto.ProjectCreateDTO;
import com.aitest.project.dto.ProjectDetailDTO;
import com.aitest.project.dto.ProjectQueryDTO;
import com.aitest.project.dto.ProjectStatisticsDTO;
import com.aitest.project.dto.ProjectUpdateDTO;
import com.aitest.common.entity.BaseEntity;
import com.aitest.project.entity.Project;
import com.aitest.project.entity.ProjectMember;
import com.aitest.project.mapper.ProjectMapper;
import com.aitest.project.service.ProjectMemberService;
import com.aitest.project.service.ProjectService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 项目Service实现类
 */
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectServiceImpl.class);

    @Autowired
    private ProjectMemberService projectMemberService;

    @Override
    public IPage<Project> getProjectPage(ProjectQueryDTO queryDTO) {
        Page<Project> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        
        LambdaQueryWrapper<Project> wrapper = new LambdaQueryWrapper<>();
        
        // 按名称模糊查询
        if (queryDTO.getName() != null && !queryDTO.getName().isEmpty()) {
            wrapper.like(Project::getName, queryDTO.getName());
        }
        
        // 按状态查询
        if (queryDTO.getStatus() != null && !queryDTO.getStatus().isEmpty()) {
            wrapper.eq(Project::getStatus, queryDTO.getStatus());
        }
        
        // 按可见性查询
        if (queryDTO.getVisibility() != null && !queryDTO.getVisibility().isEmpty()) {
            wrapper.eq(Project::getVisibility, queryDTO.getVisibility());
        }
        
        // 按负责人查询
        if (queryDTO.getOwnerId() != null) {
            wrapper.eq(Project::getOwnerId, queryDTO.getOwnerId());
        }
        
        // 按创建时间倒序
        wrapper.orderByDesc(BaseEntity::getCreateTime);
        
        return this.page(page, wrapper);
    }

    @Override
    public ProjectDetailDTO getProjectDetail(Long projectId) {
        Project project = this.getById(projectId);
        if (project == null) {
            return null;
        }
        
        ProjectDetailDTO detailDTO = new ProjectDetailDTO();
        detailDTO.setProject(project);
        
        // 获取项目成员
        detailDTO.setMembers(projectMemberService.getMembersByProjectId(projectId));
        
        return detailDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Project createProject(ProjectCreateDTO createDTO) {
        // 检查项目名称是否已存在
        if (checkNameExists(createDTO.getName(), null)) {
            throw new RuntimeException("项目名称已存在");
        }
        
        Project project = new Project();
        BeanUtils.copyProperties(createDTO, project);
        
        // 自动生成项目code（如果未提供）
        if (project.getCode() == null || project.getCode().isEmpty()) {
            project.setCode(generateProjectCode());
        }
        
        // 状态值映射：支持前端传递的多种状态值
        String status = createDTO.getStatus();
        if (status == null || status.isEmpty()) {
            project.setStatus("active");
        } else {
            project.setStatus(mapStatus(status));
        }
        
        // 可见性默认值
        if (project.getVisibility() == null || project.getVisibility().isEmpty()) {
            project.setVisibility("private");
        }
        
        // 保存项目
        this.save(project);
        
        // 将创建者添加为项目成员（owner角色）
        if (project.getOwnerId() != null) {
            ProjectMember ownerMember = new ProjectMember();
            ownerMember.setProjectId(project.getId());
            ownerMember.setUserId(project.getOwnerId());
            ownerMember.setRole("owner");
            ownerMember.setJoinedAt(LocalDateTime.now());
            ownerMember.setCreatedBy(project.getOwnerId());
            projectMemberService.save(ownerMember);
        }
        
        LOGGER.info("创建项目成功: {}", project.getName());
        return project;
    }
    
    /**
     * 状态值映射：将前端状态值映射到后端状态值
     * 前端状态: PLANNING, IN_PROGRESS, COMPLETED, ARCHIVED, ACTIVE
     * 后端状态: active, archived
     */
    private String mapStatus(String status) {
        if (status == null) {
            return "active";
        }
        String upperStatus = status.toUpperCase();
        switch (upperStatus) {
            case "ARCHIVED":
            case "COMPLETED":
                return "archived";
            case "PLANNING":
            case "IN_PROGRESS":
            case "ACTIVE":
            default:
                return "active";
        }
    }
    
    /**
     * 生成项目唯一标识code
     * 格式: PROJ + 时间戳 + 随机数
     */
    private String generateProjectCode() {
        String timestamp = String.valueOf(System.currentTimeMillis()).substring(8);
        String random = String.format("%03d", (int)(Math.random() * 1000));
        return "PROJ" + timestamp + random;
    }

    @Override
    public Project updateProject(Long projectId, ProjectUpdateDTO updateDTO) {
        Project project = this.getById(projectId);
        if (project == null) {
            throw new RuntimeException("项目不存在");
        }
        
        BeanUtils.copyProperties(updateDTO, project);
        this.updateById(project);
        
        LOGGER.info("更新项目成功: {}", projectId);
        return project;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteProject(Long projectId) {
        // 先删除项目成员
        LambdaQueryWrapper<ProjectMember> memberWrapper = new LambdaQueryWrapper<>();
        memberWrapper.eq(ProjectMember::getProjectId, projectId);
        projectMemberService.remove(memberWrapper);
        
        // 再删除项目
        boolean result = this.removeById(projectId);
        
        LOGGER.info("删除项目成功: {}", projectId);
        return result;
    }

    @Override
    public boolean archiveProject(Long projectId) {
        Project project = this.getById(projectId);
        if (project == null) {
            throw new RuntimeException("项目不存在");
        }
        
        project.setStatus("archived");
        boolean result = this.updateById(project);
        
        LOGGER.info("归档项目成功: {}", projectId);
        return result;
    }

    @Override
    public boolean unarchiveProject(Long projectId) {
        Project project = this.getById(projectId);
        if (project == null) {
            throw new RuntimeException("项目不存在");
        }
        
        project.setStatus("active");
        boolean result = this.updateById(project);
        
        LOGGER.info("取消归档项目成功: {}", projectId);
        return result;
    }

    @Override
    public ProjectStatisticsDTO getStatistics() {
        ProjectStatisticsDTO statistics = new ProjectStatisticsDTO();
        
        // 项目总数
        statistics.setTotalProjects(this.count());
        
        // 活跃项目数
        LambdaQueryWrapper<Project> activeWrapper = new LambdaQueryWrapper<>();
        activeWrapper.eq(Project::getStatus, "active");
        statistics.setActiveProjects(this.count(activeWrapper));
        
        // 归档项目数
        LambdaQueryWrapper<Project> archivedWrapper = new LambdaQueryWrapper<>();
        archivedWrapper.eq(Project::getStatus, "archived");
        statistics.setArchivedProjects(this.count(archivedWrapper));
        
        // 项目成员总数
        statistics.setTotalMembers(projectMemberService.count());
        
        return statistics;
    }

    @Override
    public boolean checkCodeExists(String code, Long excludeId) {
        QueryWrapper<Project> wrapper = new QueryWrapper<>();
        wrapper.eq("code", code);
        
        if (excludeId != null) {
            wrapper.ne("id", excludeId);
        }
        
        return this.count(wrapper) > 0;
    }
    
    @Override
    public boolean checkNameExists(String name, Long excludeId) {
        QueryWrapper<Project> wrapper = new QueryWrapper<>();
        wrapper.eq("name", name);
        
        if (excludeId != null) {
            wrapper.ne("id", excludeId);
        }
        
        return this.count(wrapper) > 0;
    }
}
