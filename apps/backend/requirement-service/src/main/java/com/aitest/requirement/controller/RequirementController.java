package com.aitest.requirement.controller;

import com.aitest.common.result.Result;
import com.aitest.requirement.dto.RequirementCreateDTO;
import com.aitest.requirement.dto.RequirementQueryDTO;
import com.aitest.requirement.dto.RequirementUpdateDTO;
import com.aitest.requirement.entity.Requirement;
import com.aitest.requirement.service.FileStorageService;
import com.aitest.requirement.service.RequirementService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 需求管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/requirements")
public class RequirementController {

    @Autowired
    private RequirementService requirementService;

    @Autowired
    private FileStorageService fileStorageService;

    /**
     * 健康检查接口
     */
    @GetMapping("/health")
    public Result<Map<String, Object>> health() {
        Map<String, Object> data = new HashMap<>();
        data.put("status", "UP");
        data.put("service", "requirement-service");
        return Result.success(data);
    }

    /**
     * 查询需求列表（兼容前端调用）
     */
    @GetMapping
    public Result<Map<String, Object>> listRequirements(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) Long project_id,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        
        RequirementQueryDTO dto = new RequirementQueryDTO();
        
        // 兼容搜索关键词
        if (name != null && !name.isEmpty()) {
            dto.setKeyword(name);
        } else if (title != null && !title.isEmpty()) {
            dto.setKeyword(title);
        }
        
        // 兼容项目ID
        if (projectId != null) {
            dto.setProjectId(projectId);
        } else if (project_id != null) {
            dto.setProjectId(project_id);
        }
        
        dto.setStatus(status);
        dto.setPriority(priority);
        
        // 兼容分页参数
        dto.setPageNum(page);
        dto.setPageSize(size);
        
        IPage<Requirement> pageResult = requirementService.queryRequirements(dto);
        Map<String, Object> data = new HashMap<>();
        data.put("list", pageResult.getRecords());
        data.put("total", pageResult.getTotal());
        data.put("page", pageResult.getCurrent());
        data.put("size", pageResult.getSize());
        return Result.success(data);
    }

    /**
     * 创建需求
     */
    @PostMapping
    public Result<Requirement> createRequirement(@RequestBody RequirementCreateDTO dto) {
        // 先处理下划线字段兼容
        handleUnderscoreFields(dto);
        // 再进行参数校验
        validateCreateDTO(dto);
        
        log.info("创建需求请求: title={}, projectId={}", dto.getTitle(), dto.getProjectId());
        
        try {
            Requirement requirement = requirementService.createRequirement(dto);
            log.info("创建需求成功: id={}", requirement.getId());
            return Result.success(requirement);
        } catch (IllegalArgumentException e) {
            log.warn("创建需求参数异常: {}", e.getMessage());
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("创建需求系统异常", e);
            return Result.error("创建失败，请稍后重试");
        }
    }

    /**
     * 更新需求（POST方式，兼容前端调用）
     */
    @PostMapping("/{id}")
    public Result<Requirement> updateRequirementByPost(@PathVariable Long id, @RequestBody RequirementUpdateDTO dto) {
        // 先处理下划线字段兼容
        handleUnderscoreFields(dto);
        dto.setId(id);
        // 再校验参数
        validateUpdateDTO(dto);
        
        log.info("更新需求请求(id={}): title={}", id, dto.getTitle());
        
        try {
            Requirement requirement = requirementService.updateRequirement(dto);
            log.info("更新需求成功: id={}", id);
            return Result.success(requirement);
        } catch (IllegalArgumentException e) {
            log.warn("更新需求参数异常: {}", e.getMessage());
            return Result.error(400, e.getMessage());
        } catch (RuntimeException e) {
            log.warn("更新需求业务异常: {}", e.getMessage());
            return Result.error(404, e.getMessage());
        } catch (Exception e) {
            log.error("更新需求系统异常", e);
            return Result.error("更新失败，请稍后重试");
        }
    }

    /**
     * 更新需求（PUT方式）
     */
    @PutMapping
    public Result<Requirement> updateRequirement(@RequestBody RequirementUpdateDTO dto) {
        // 先处理下划线字段兼容
        handleUnderscoreFields(dto);
        // 再校验参数
        validateUpdateDTO(dto);
        
        log.info("更新需求请求(id={}): title={}", dto.getId(), dto.getTitle());
        
        try {
            Requirement requirement = requirementService.updateRequirement(dto);
            log.info("更新需求成功: id={}", dto.getId());
            return Result.success(requirement);
        } catch (IllegalArgumentException e) {
            log.warn("更新需求参数异常: {}", e.getMessage());
            return Result.error(400, e.getMessage());
        } catch (RuntimeException e) {
            log.warn("更新需求业务异常: {}", e.getMessage());
            return Result.error(404, e.getMessage());
        } catch (Exception e) {
            log.error("更新需求系统异常", e);
            return Result.error("更新失败，请稍后重试");
        }
    }

    /**
     * 删除需求
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteRequirement(@PathVariable Long id) {
        try {
            boolean success = requirementService.deleteRequirement(id);
            return success ? Result.success() : Result.error("删除失败");
        } catch (Exception e) {
            log.error("删除需求失败", e);
            return Result.error("删除失败，请稍后重试");
        }
    }

    /**
     * 批量删除需求
     */
    @DeleteMapping("/batch")
    public Result<Void> batchDeleteRequirements(@RequestBody List<Long> ids) {
        try {
            boolean success = requirementService.batchDeleteRequirements(ids);
            return success ? Result.success() : Result.error("批量删除失败");
        } catch (Exception e) {
            log.error("批量删除需求失败", e);
            return Result.error("删除失败，请稍后重试");
        }
    }

    /**
     * 获取需求详情
     */
    @GetMapping("/{id}")
    public Result<Requirement> getRequirementById(@PathVariable Long id) {
        Requirement requirement = requirementService.getRequirementById(id);
        return Result.success(requirement);
    }

    /**
     * 分页查询需求列表
     */
    @GetMapping("/page")
    public Result<IPage<Requirement>> queryRequirements(RequirementQueryDTO dto) {
        IPage<Requirement> page = requirementService.queryRequirements(dto);
        return Result.success(page);
    }

    /**
     * 查询项目下的所有需求
     */
    @GetMapping("/project/{projectId}")
    public Result<List<Requirement>> getRequirementsByProjectId(@PathVariable Long projectId) {
        List<Requirement> list = requirementService.getRequirementsByProjectId(projectId);
        return Result.success(list);
    }

    /**
     * 上传需求文档
     */
    @PostMapping("/upload")
    public Result<Map<String, String>> uploadDocument(@RequestParam("file") MultipartFile file) {
        try {
            String filename = fileStorageService.uploadFile(file);
            Map<String, String> data = new HashMap<>();
            data.put("filename", filename);
            data.put("originalFilename", file.getOriginalFilename());
            data.put("size", String.valueOf(file.getSize()));
            return Result.success("上传成功", data);
        } catch (IOException e) {
            log.error("文件上传失败", e);
            return Result.fail("文件上传失败: " + e.getMessage());
        }
    }

    /**
     * 下载需求文档
     */
    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> downloadDocument(@PathVariable String filename) {
        try {
            Resource resource = fileStorageService.downloadFile(filename);
            
            String contentType = "application/octet-stream";
            String encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8.toString()).replace("+", "%20");
            
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFilename)
                    .body(resource);
        } catch (Exception e) {
            log.error("文件下载失败", e);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 更新需求文档关联
     */
    @PutMapping("/{id}/document")
    public Result<Requirement> updateDocument(@PathVariable Long id, @RequestParam("filename") String filename) {
        Requirement requirement = requirementService.getRequirementById(id);
        if (requirement == null) {
            return Result.fail("需求不存在");
        }
        requirement.setDocumentUrl(filename);
        requirementService.updateById(requirement);
        return Result.success(requirement);
    }

    /**
     * 处理CreateDTO的下划线字段兼容
     */
    private void handleUnderscoreFields(RequirementCreateDTO dto) {
        // 兼容 project_id
        if (dto.getProjectId() == null && dto.getProject_id() != null) {
            dto.setProjectId(dto.getProject_id());
        }
        // 兼容 name字段到title
        if ((dto.getTitle() == null || dto.getTitle().isEmpty()) && dto.getName() != null && !dto.getName().isEmpty()) {
            dto.setTitle(dto.getName());
        }
        // 兼容其他下划线字段
        if (dto.getProposerTime() == null && dto.getProposer_time() != null) {
            dto.setProposerTime(dto.getProposer_time());
        }
        if (dto.getEffectiveVersion() == null && dto.getEffective_version() != null) {
            dto.setEffectiveVersion(dto.getEffective_version());
        }
        if (dto.getAcceptanceCriteria() == null && dto.getAcceptance_criteria() != null) {
            dto.setAcceptanceCriteria(dto.getAcceptance_criteria());
        }
        if (dto.getPermissionScope() == null && dto.getPermission_scope() != null) {
            dto.setPermissionScope(dto.getPermission_scope());
        }
        if (dto.getReviewResult() == null && dto.getReview_result() != null) {
            dto.setReviewResult(dto.getReview_result());
        }
        if (dto.getReviewComments() == null && dto.getReview_comments() != null) {
            dto.setReviewComments(dto.getReview_comments());
        }
        if (dto.getOnlineTime() == null && dto.getOnline_time() != null) {
            dto.setOnlineTime(dto.getOnline_time());
        }
        if (dto.getCloseReason() == null && dto.getClose_reason() != null) {
            dto.setCloseReason(dto.getClose_reason());
        }
    }

    /**
     * 处理UpdateDTO的下划线字段兼容
     */
    private void handleUnderscoreFields(RequirementUpdateDTO dto) {
        // 兼容 project_id
        if (dto.getProjectId() == null && dto.getProject_id() != null) {
            dto.setProjectId(dto.getProject_id());
        }
        // 兼容 name字段到title
        if ((dto.getTitle() == null || dto.getTitle().isEmpty()) && dto.getName() != null && !dto.getName().isEmpty()) {
            dto.setTitle(dto.getName());
        }
        // 兼容其他下划线字段
        if (dto.getProposerTime() == null && dto.getProposer_time() != null) {
            dto.setProposerTime(dto.getProposer_time());
        }
        if (dto.getEffectiveVersion() == null && dto.getEffective_version() != null) {
            dto.setEffectiveVersion(dto.getEffective_version());
        }
        if (dto.getAcceptanceCriteria() == null && dto.getAcceptance_criteria() != null) {
            dto.setAcceptanceCriteria(dto.getAcceptance_criteria());
        }
        if (dto.getPermissionScope() == null && dto.getPermission_scope() != null) {
            dto.setPermissionScope(dto.getPermission_scope());
        }
        if (dto.getReviewResult() == null && dto.getReview_result() != null) {
            dto.setReviewResult(dto.getReview_result());
        }
        if (dto.getReviewComments() == null && dto.getReview_comments() != null) {
            dto.setReviewComments(dto.getReview_comments());
        }
        if (dto.getOnlineTime() == null && dto.getOnline_time() != null) {
            dto.setOnlineTime(dto.getOnline_time());
        }
        if (dto.getCloseReason() == null && dto.getClose_reason() != null) {
            dto.setCloseReason(dto.getClose_reason());
        }
    }

    /**
     * 校验CreateDTO参数
     */
    private void validateCreateDTO(RequirementCreateDTO dto) {
        if (dto.getProjectId() == null) {
            throw new IllegalArgumentException("项目ID不能为空");
        }
        if (dto.getTitle() == null || dto.getTitle().isEmpty()) {
            throw new IllegalArgumentException("需求标题不能为空");
        }
    }

    /**
     * 校验UpdateDTO参数
     */
    private void validateUpdateDTO(RequirementUpdateDTO dto) {
        if (dto.getId() == null) {
            throw new IllegalArgumentException("需求ID不能为空");
        }
        // title在update时可选，但如果提供了就不能为空
        if (dto.getTitle() != null && dto.getTitle().isEmpty()) {
            throw new IllegalArgumentException("需求标题不能为空");
        }
    }
}
