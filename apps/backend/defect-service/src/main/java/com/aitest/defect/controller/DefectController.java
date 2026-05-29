package com.aitest.defect.controller;

import com.aitest.common.result.Result;
import com.aitest.defect.dto.DefectCreateDTO;
import com.aitest.defect.dto.DefectQueryDTO;
import com.aitest.defect.entity.Defect;
import com.aitest.defect.service.DefectService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 缺陷控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/defects")
public class DefectController {

    @Autowired
    private DefectService defectService;

    /**
     * 健康检查接口
     */
    @GetMapping("/health")
    public Result<Map<String, Object>> health() {
        Map<String, Object> data = new HashMap<>();
        data.put("status", "UP");
        data.put("service", "defect-service");
        return Result.success(data);
    }

    /**
     * 获取缺陷列表
     */
    @GetMapping
    public Result<Map<String, Object>> listDefects(DefectQueryDTO dto) {
        IPage<Defect> page = defectService.queryDefects(dto);
        List<Map<String, Object>> transformedList = new ArrayList<>();
        for (Defect defect : page.getRecords()) {
            transformedList.add(transformDefect(defect));
        }
        Map<String, Object> data = new HashMap<>();
        data.put("list", transformedList);
        data.put("total", page.getTotal());
        data.put("page", page.getCurrent());
        data.put("size", page.getSize());
        return Result.success(data);
    }

    /**
     * 获取缺陷统计
     */
    @GetMapping("/stats")
    public Result<Map<String, Object>> getStatistics(@RequestParam(required = false) Long projectId) {
        Map<String, Object> stats = defectService.getStatistics(projectId);
        return Result.success(stats);
    }

    /**
     * 获取缺陷详情
     */
    @GetMapping("/{id}")
    public Result<Map<String, Object>> getDefectDetail(@PathVariable Long id) {
        Defect defect = defectService.getDefectById(id);
        if (defect == null) {
            return Result.error("缺陷不存在");
        }
        return Result.success(transformDefect(defect));
    }

    /**
     * 创建缺陷
     */
    @PostMapping
    public Result<Map<String, Object>> createDefect(@Valid @RequestBody DefectCreateDTO dto) {
        try {
            Defect defect = defectService.createDefect(dto);
            return Result.success(transformDefect(defect));
        } catch (Exception e) {
            log.error("创建缺陷失败", e);
            return Result.error("创建失败，请稍后重试");
        }
    }

    /**
     * 更新缺陷（POST方式，兼容前端调用）
     */
    @PostMapping("/{id}")
    public Result<Map<String, Object>> updateDefectByPost(@PathVariable Long id, @RequestBody DefectCreateDTO dto) {
        try {
            Defect defect = defectService.updateDefect(id, dto);
            if (defect == null) {
                return Result.error("缺陷不存在");
            }
            return Result.success(transformDefect(defect));
        } catch (Exception e) {
            log.error("更新缺陷失败", e);
            return Result.error("更新失败，请稍后重试");
        }
    }

    /**
     * 更新缺陷（PUT方式）
     */
    @PutMapping("/{id}")
    public Result<Map<String, Object>> updateDefect(@PathVariable Long id, @RequestBody DefectCreateDTO dto) {
        try {
            Defect defect = defectService.updateDefect(id, dto);
            if (defect == null) {
                return Result.error("缺陷不存在");
            }
            return Result.success(transformDefect(defect));
        } catch (Exception e) {
            log.error("更新缺陷失败", e);
            return Result.error("更新失败，请稍后重试");
        }
    }

    /**
     * 删除缺陷
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteDefect(@PathVariable Long id) {
        try {
            boolean success = defectService.deleteDefect(id);
            return success ? Result.success() : Result.error("删除失败");
        } catch (Exception e) {
            log.error("删除缺陷失败", e);
            return Result.error("删除失败，请稍后重试");
        }
    }

    /**
     * 批量删除缺陷
     */
    @DeleteMapping("/batch")
    public Result<Void> batchDeleteDefects(@RequestBody List<Long> ids) {
        try {
            boolean success = defectService.batchDeleteDefects(ids);
            return success ? Result.success() : Result.error("删除失败");
        } catch (Exception e) {
            log.error("批量删除缺陷失败", e);
            return Result.error("删除失败，请稍后重试");
        }
    }

    /**
     * 分配缺陷
     */
    @PostMapping("/{id}/assign")
    public Result<Map<String, Object>> assignDefect(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        Object handlerObj = body.get("handler");
        Long assignee = null;
        if (handlerObj != null) {
            if (handlerObj instanceof Number) {
                assignee = ((Number) handlerObj).longValue();
            } else if (handlerObj instanceof String) {
                try {
                    assignee = Long.parseLong((String) handlerObj);
                } catch (NumberFormatException e) {
                    // 字符串不能转换为数字时，assignee保持为null
                }
            }
        }
        Defect defect = defectService.assignDefect(id, assignee);
        if (defect == null) {
            return Result.error("缺陷不存在");
        }
        return Result.success(transformDefect(defect));
    }

    /**
     * 查询项目下的缺陷
     */
    @GetMapping("/project/{projectId}")
    public Result<List<Map<String, Object>>> getDefectsByProjectId(@PathVariable Long projectId) {
        List<Defect> defects = defectService.getDefectsByProjectId(projectId);
        List<Map<String, Object>> transformedList = new ArrayList<>();
        for (Defect defect : defects) {
            transformedList.add(transformDefect(defect));
        }
        return Result.success(transformedList);
    }

    /**
     * 实体转前端格式（字段名转换）
     */
    private Map<String, Object> transformDefect(Defect defect) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", defect.getId());
        map.put("title", defect.getTitle());
        map.put("description", defect.getDescription());
        map.put("priority", defect.getPriority());
        map.put("status", defect.getStatus());
        map.put("type", defect.getType());
        map.put("severity", defect.getSeverity());
        map.put("module", defect.getModule());
        map.put("steps", defect.getStepsToReproduce());
        map.put("stepsToReproduce", defect.getStepsToReproduce());
        map.put("expectedResult", defect.getExpectedResult());
        map.put("actualResult", defect.getActualResult());
        map.put("requirementId", defect.getRequirementId());
        map.put("requirement_id", defect.getRequirementId());
        map.put("projectId", defect.getProjectId());
        map.put("project_id", defect.getProjectId());
        map.put("assignee", defect.getAssignee());
        map.put("assignee_id", defect.getAssignee());
        map.put("reporterId", defect.getReporterId());
        map.put("reporter_id", defect.getReporterId());
        map.put("createdBy", defect.getCreatedBy());
        map.put("created_at", defect.getCreateTime());
        map.put("createTime", defect.getCreateTime());
        map.put("updatedAt", defect.getUpdateTime());
        map.put("updated_at", defect.getUpdateTime());
        map.put("updateTime", defect.getUpdateTime());
        map.put("projectName", defect.getProjectName());
        map.put("project_name", defect.getProjectName());
        map.put("requirementName", defect.getRequirementName());
        map.put("requirement_name", defect.getRequirementName());
        map.put("handler", defect.getHandler());
        map.put("reporterName", defect.getReporterName());
        return map;
    }
}
