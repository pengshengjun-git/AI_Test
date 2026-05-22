package com.aitest.project.controller;

import com.aitest.common.result.Result;
import com.aitest.project.dto.ProjectCreateDTO;
import com.aitest.project.dto.ProjectDetailDTO;
import com.aitest.project.dto.ProjectQueryDTO;
import com.aitest.project.dto.ProjectResponseDTO;
import com.aitest.project.dto.ProjectStatisticsDTO;
import com.aitest.project.dto.ProjectUpdateDTO;
import com.aitest.project.entity.Project;
import com.aitest.project.service.ProjectService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 项目控制器
 */
@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    private ProjectService projectService;

    /**
     * 健康检查接口
     */
    @GetMapping("/health")
    public Result<Map<String, Object>> health() {
        Map<String, Object> data = new HashMap<>();
        data.put("status", "UP");
        data.put("service", "project-service");
        return Result.success(data);
    }

    /**
     * 分页查询项目列表（兼容前端调用）
     */
    @GetMapping
    public Result<Map<String, Object>> listProjects(ProjectQueryDTO queryDTO) {
        IPage<Project> page = projectService.getProjectPage(queryDTO);
        Map<String, Object> data = new HashMap<>();
        // 将实体转换为响应DTO，进行状态值转换
        List<ProjectResponseDTO> responseList = page.getRecords().stream()
                .map(ProjectResponseDTO::fromEntity)
                .collect(Collectors.toList());
        data.put("list", responseList);
        data.put("total", page.getTotal());
        data.put("page", page.getCurrent());
        data.put("size", page.getSize());
        return Result.success(data);
    }

    /**
     * 获取项目详情
     */
    @GetMapping("/{id}")
    public Result<ProjectDetailDTO> getProjectDetail(@PathVariable Long id) {
        ProjectDetailDTO detail = projectService.getProjectDetail(id);
        if (detail == null) {
            Result<ProjectDetailDTO> result = new Result<>();
            result.setCode(500);
            result.setMessage("项目不存在");
            return result;
        }
        return Result.success(detail);
    }

    /**
     * 创建项目
     */
    @PostMapping
    public Result<ProjectResponseDTO> createProject(@Valid @RequestBody ProjectCreateDTO createDTO) {
        try {
            Project project = projectService.createProject(createDTO);
            return Result.success(ProjectResponseDTO.fromEntity(project));
        } catch (Exception e) {
            LOGGER.error("创建项目失败", e);
            Result<ProjectResponseDTO> result = new Result<>();
            // 业务逻辑错误（如名称重复）返回400，其他错误返回500
            if (e.getMessage() != null && e.getMessage().contains("项目名称已存在")) {
                result.setCode(400);
            } else {
                result.setCode(500);
            }
            result.setMessage(e.getMessage());
            return result;
        }
    }

    /**
     * 更新项目（POST方式，兼容前端调用）
     */
    @PostMapping("/{id}")
    public Result<Project> updateProjectByPost(@PathVariable Long id, @RequestBody ProjectUpdateDTO updateDTO) {
        try {
            Project project = projectService.updateProject(id, updateDTO);
            return Result.success(project);
        } catch (Exception e) {
            LOGGER.error("更新项目失败", e);
            Result<Project> result = new Result<>();
            result.setCode(500);
            result.setMessage(e.getMessage());
            return result;
        }
    }

    /**
     * 更新项目（PUT方式）
     */
    @PutMapping("/{id}")
    public Result<Project> updateProject(@PathVariable Long id, @RequestBody ProjectUpdateDTO updateDTO) {
        try {
            Project project = projectService.updateProject(id, updateDTO);
            return Result.success(project);
        } catch (Exception e) {
            LOGGER.error("更新项目失败", e);
            Result<Project> result = new Result<>();
            result.setCode(500);
            result.setMessage(e.getMessage());
            return result;
        }
    }

    /**
     * 删除项目
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteProject(@PathVariable Long id) {
        try {
            boolean result = projectService.deleteProject(id);
            if (result) {
                return Result.success();
            } else {
                return Result.fail("删除失败");
            }
        } catch (Exception e) {
            LOGGER.error("删除项目失败", e);
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 归档项目
     */
    @PutMapping("/{id}/archive")
    public Result<Void> archiveProject(@PathVariable Long id) {
        try {
            boolean result = projectService.archiveProject(id);
            if (result) {
                return Result.success();
            } else {
                return Result.fail("归档失败");
            }
        } catch (Exception e) {
            LOGGER.error("归档项目失败", e);
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 取消归档项目
     */
    @PutMapping("/{id}/unarchive")
    public Result<Void> unarchiveProject(@PathVariable Long id) {
        try {
            boolean result = projectService.unarchiveProject(id);
            if (result) {
                return Result.success();
            } else {
                return Result.fail("取消归档失败");
            }
        } catch (Exception e) {
            LOGGER.error("取消归档项目失败", e);
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 获取项目统计信息
     */
    @GetMapping("/statistics")
    public Result<ProjectStatisticsDTO> getStatistics() {
        ProjectStatisticsDTO statistics = projectService.getStatistics();
        return Result.success(statistics);
    }
}
