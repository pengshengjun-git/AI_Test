package com.aitest.project.service.impl;

import com.aitest.project.dto.ProjectCreateDTO;
import com.aitest.project.dto.ProjectDetailDTO;
import com.aitest.project.dto.ProjectQueryDTO;
import com.aitest.project.dto.ProjectStatisticsDTO;
import com.aitest.project.dto.ProjectUpdateDTO;
import com.aitest.common.entity.BaseEntity;
import com.aitest.project.entity.Project;
import com.aitest.project.mapper.ProjectMapper;
import com.aitest.project.service.ProjectService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Map;
import java.util.HashMap;

/**
 * 项目Service实现类
 */
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectServiceImpl.class);
    
    private final JdbcTemplate jdbcTemplate;
    
    public ProjectServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public IPage<Project> getProjectPage(ProjectQueryDTO queryDTO) {
        Page<Project> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        
        LambdaQueryWrapper<Project> wrapper = new LambdaQueryWrapper<>();
        
        // 按名称模糊查询
        if (queryDTO.getName() != null && !queryDTO.getName().isEmpty()) {
            wrapper.like(Project::getName, queryDTO.getName());
        }
        
        // 按状态查询 - 需要处理前端状态到数据库状态的映射
        if (queryDTO.getStatus() != null && !queryDTO.getStatus().isEmpty()) {
            String dbStatus = mapToDbStatus(queryDTO.getStatus());
            wrapper.eq(Project::getStatus, dbStatus);
        }
        
        // 按创建时间倒序
        wrapper.orderByDesc(BaseEntity::getCreateTime);
        
        IPage<Project> resultPage = this.page(page, wrapper);
        
        // 获取真实统计数据
        resultPage.getRecords().forEach(project -> {
            project.setTestcaseCount(getTestcaseCount(project.getId()));
            project.setDefectCount(getDefectCount(project.getId()));
            project.setRequirementCount(getRequirementCount(project.getId()));
            // 获取创建人用户名
            if (project.getCreatedBy() != null) {
                String username = getUserName(project.getCreatedBy());
                project.setOwnerName(username != null ? username : "用户" + project.getCreatedBy());
            }
        });
        
        return resultPage;
    }
    
    /**
     * 从数据库直接获取项目的测试用例数（包括通过需求关联的）
     */
    private Integer getTestcaseCount(Long projectId) {
        try {
            String sql = "SELECT COUNT(*) FROM testcase WHERE deleted = 0 AND " +
                        "(project_id = ? OR EXISTS (SELECT 1 FROM requirement r WHERE r.id = testcase.requirement_id AND r.project_id = ? AND r.deleted = 0))";
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, projectId, projectId);
            return count != null ? count : 0;
        } catch (Exception e) {
            LOGGER.warn("获取测试用例统计失败: projectId={}, error={}", projectId, e.getMessage());
            return 0;
        }
    }
    
    /**
     * 从数据库直接获取项目的缺陷数（包括通过需求关联的）
     */
    private Integer getDefectCount(Long projectId) {
        try {
            String sql = "SELECT COUNT(*) FROM defect WHERE deleted = 0 AND " +
                        "(project_id = ? OR EXISTS (SELECT 1 FROM requirement r WHERE r.id = defect.requirement_id AND r.project_id = ? AND r.deleted = 0))";
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, projectId, projectId);
            return count != null ? count : 0;
        } catch (Exception e) {
            LOGGER.warn("获取缺陷统计失败: projectId={}, error={}", projectId, e.getMessage());
            return 0;
        }
    }
    
    /**
     * 从数据库直接获取项目的需求数
     */
    private Integer getRequirementCount(Long projectId) {
        try {
            String sql = "SELECT COUNT(*) FROM requirement WHERE project_id = ? AND deleted = 0";
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, projectId);
            return count != null ? count : 0;
        } catch (Exception e) {
            LOGGER.warn("获取需求统计失败: projectId={}, error={}", projectId, e.getMessage());
            return 0;
        }
    }
    
    /**
     * 从数据库直接获取用户名
     */
    private String getUserName(Long userId) {
        try {
            String sql = "SELECT username FROM user WHERE id = ? AND deleted = 0";
            String username = jdbcTemplate.queryForObject(sql, String.class, userId);
            return username;
        } catch (Exception e) {
            LOGGER.warn("获取用户名失败: userId={}, error={}", userId, e.getMessage());
            return null;
        }
    }

    @Override
    public ProjectDetailDTO getProjectDetail(Long projectId) {
        Project project = this.getById(projectId);
        if (project == null) {
            return null;
        }
        
        ProjectDetailDTO detailDTO = new ProjectDetailDTO();
        detailDTO.setProject(project);
        
        // 项目成员功能暂时禁用
        detailDTO.setMembers(java.util.Collections.emptyList());
        
        return detailDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Project createProject(ProjectCreateDTO createDTO) {
        // 检查项目名称是否已存在
        if (checkNameExists(createDTO.getName(), null)) {
            throw new RuntimeException("项目名称已存在");
        }
        
        // 检查项目编码是否已存在（如果有填写）
        if (createDTO.getCode() != null && !createDTO.getCode().isEmpty() && checkCodeExists(createDTO.getCode(), null)) {
            throw new RuntimeException("项目编码已存在");
        }
        
        Project project = new Project();
        BeanUtils.copyProperties(createDTO, project);
        
        // 自动生成项目编码
        if (project.getCode() == null || project.getCode().isEmpty()) {
            project.setCode(generateProjectCode());
        }
        
        // 状态映射 - 前端状态到数据库状态
        if (project.getStatus() != null && !project.getStatus().isEmpty()) {
            project.setStatus(mapToDbStatus(project.getStatus()));
        } else {
            project.setStatus("PLANNING");
        }
        
        // 优先级默认值
        if (project.getPriority() == null || project.getPriority().isEmpty()) {
            project.setPriority("P2");
        }
        
        // 创建人ID（暂时固定为1）
        project.setCreatedBy(1L);
        
        // 保存项目
        this.save(project);
        
        LOGGER.info("创建项目成功: {}", project.getName());
        return project;
    }
    
    /**
     * 生成项目编码
     * 格式: PROJ + 时间戳后6位 + 4位随机数
     */
    private String generateProjectCode() {
        String timestamp = String.valueOf(System.currentTimeMillis()).substring(6);
        String random = String.format("%04d", (int)(Math.random() * 10000));
        return "PROJ" + timestamp + random;
    }

    @Override
    public Project updateProject(Long projectId, ProjectUpdateDTO updateDTO) {
        Project project = this.getById(projectId);
        if (project == null) {
            throw new RuntimeException("项目不存在");
        }
        
        // 检查项目名称是否已存在（排除当前项目）
        if (updateDTO.getName() != null && !updateDTO.getName().isEmpty() && checkNameExists(updateDTO.getName(), projectId)) {
            throw new RuntimeException("项目名称已存在");
        }
        
        // 检查项目编码是否已存在（排除当前项目）
        if (updateDTO.getCode() != null && !updateDTO.getCode().isEmpty() && checkCodeExists(updateDTO.getCode(), projectId)) {
            throw new RuntimeException("项目编码已存在");
        }
        
        // 更新时也需要做状态映射
        if (updateDTO.getStatus() != null && !updateDTO.getStatus().isEmpty()) {
            updateDTO.setStatus(mapToDbStatus(updateDTO.getStatus()));
        }
        
        BeanUtils.copyProperties(updateDTO, project);
        this.updateById(project);
        
        LOGGER.info("更新项目成功: {}", projectId);
        return project;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteProject(Long projectId) {
        // 删除项目
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
        
        project.setStatus("ARCHIVED");
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
        
        project.setStatus("PLANNING");
        boolean result = this.updateById(project);
        
        LOGGER.info("取消归档项目成功: {}", projectId);
        return result;
    }

    @Override
    public ProjectStatisticsDTO getStatistics() {
        ProjectStatisticsDTO statistics = new ProjectStatisticsDTO();
        
        // 项目总数
        statistics.setTotalProjects(this.count());
        
        // 活跃项目数（除了已归档的）
        LambdaQueryWrapper<Project> activeWrapper = new LambdaQueryWrapper<>();
        activeWrapper.and(w -> w.ne(Project::getStatus, "ARCHIVED")
                .ne(Project::getStatus, "archived"));
        statistics.setActiveProjects(this.count(activeWrapper));
        
        // 归档项目数
        LambdaQueryWrapper<Project> archivedWrapper = new LambdaQueryWrapper<>();
        archivedWrapper.and(w -> w.eq(Project::getStatus, "ARCHIVED")
                .or().eq(Project::getStatus, "archived"));
        statistics.setArchivedProjects(this.count(archivedWrapper));
        
        // 项目成员总数（暂时设为0）
        statistics.setTotalMembers(0L);
        
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

    /**
     * 将前端状态值映射到数据库状态值
     * 前端状态: PLANNING, IN_PROGRESS, COMPLETED, ARCHIVED
     * 数据库状态: active, archived, PLANNING, IN_PROGRESS, COMPLETED, ARCHIVED
     */
    private String mapToDbStatus(String frontendStatus) {
        if (frontendStatus == null) {
            return "PLANNING";
        }
        String upperStatus = frontendStatus.toUpperCase();
        switch (upperStatus) {
            case "ARCHIVED":
                return "ARCHIVED";
            case "COMPLETED":
                return "COMPLETED";
            case "IN_PROGRESS":
                return "IN_PROGRESS";
            case "PLANNING":
                return "PLANNING";
            case "ACTIVE":
                return "IN_PROGRESS";
            default:
                return frontendStatus;
        }
    }
}
