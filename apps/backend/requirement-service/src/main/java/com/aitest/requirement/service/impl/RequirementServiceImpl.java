package com.aitest.requirement.service.impl;

import com.aitest.requirement.dto.RequirementCreateDTO;
import com.aitest.requirement.dto.RequirementQueryDTO;
import com.aitest.requirement.dto.RequirementUpdateDTO;
import com.aitest.requirement.entity.Requirement;
import com.aitest.requirement.mapper.RequirementMapper;
import com.aitest.requirement.service.RequirementService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 需求Service实现类
 */
@Slf4j
@Service
public class RequirementServiceImpl extends ServiceImpl<RequirementMapper, Requirement> implements RequirementService {

    private final JdbcTemplate jdbcTemplate;

    public RequirementServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 获取用户名
     */
    private String getUserName(Long userId) {
        if (userId == null) {
            return "-";
        }
        try {
            String sql = "SELECT username FROM user WHERE id = ? AND deleted = 0";
            String username = jdbcTemplate.queryForObject(sql, String.class, userId);
            return username != null ? username : "-";
        } catch (Exception e) {
            log.warn("获取用户名失败: userId={}, error={}", userId, e.getMessage());
            return "-";
        }
    }

    /**
     * 获取项目名称
     */
    private String getProjectName(Long projectId) {
        if (projectId == null) {
            return "-";
        }
        try {
            String sql = "SELECT name FROM project WHERE id = ? AND deleted = 0";
            String name = jdbcTemplate.queryForObject(sql, String.class, projectId);
            return name != null ? name : "-";
        } catch (Exception e) {
            log.warn("获取项目名称失败: projectId={}, error={}", projectId, e.getMessage());
            return "-";
        }
    }

    /**
     * 填充需求关联信息
     */
    private void fillRequirementInfo(Requirement requirement) {
        if (requirement == null) {
            return;
        }
        // 兼容 name = title
        requirement.setName(requirement.getTitle());
        // 填充创建人用户名
        requirement.setCreatorName(getUserName(requirement.getCreatedBy()));
        // 填充项目名称
        requirement.setProjectName(getProjectName(requirement.getProjectId()));
    }

    /**
     * 兼容 DTO 的下划线字段，复制到驼峰字段
     */
    private void copyUnderscoreFieldsToCamelCase(RequirementCreateDTO dto) {
        // 兼容 project_id
        if (dto.getProjectId() == null && dto.getProject_id() != null) {
            dto.setProjectId(dto.getProject_id());
        }
        // 兼容 proposer_time
        if (dto.getProposerTime() == null && dto.getProposer_time() != null) {
            dto.setProposerTime(dto.getProposer_time());
        }
        // 兼容 effective_version
        if (dto.getEffectiveVersion() == null && dto.getEffective_version() != null) {
            dto.setEffectiveVersion(dto.getEffective_version());
        }
        // 兼容 acceptance_criteria
        if (dto.getAcceptanceCriteria() == null && dto.getAcceptance_criteria() != null) {
            dto.setAcceptanceCriteria(dto.getAcceptance_criteria());
        }
        // 兼容 permission_scope
        if (dto.getPermissionScope() == null && dto.getPermission_scope() != null) {
            dto.setPermissionScope(dto.getPermission_scope());
        }
        // 兼容 review_result
        if (dto.getReviewResult() == null && dto.getReview_result() != null) {
            dto.setReviewResult(dto.getReview_result());
        }
        // 兼容 review_comments
        if (dto.getReviewComments() == null && dto.getReview_comments() != null) {
            dto.setReviewComments(dto.getReview_comments());
        }
        // 兼容 online_time
        if (dto.getOnlineTime() == null && dto.getOnline_time() != null) {
            dto.setOnlineTime(dto.getOnline_time());
        }
        // 兼容 close_reason
        if (dto.getCloseReason() == null && dto.getClose_reason() != null) {
            dto.setCloseReason(dto.getClose_reason());
        }
    }

    /**
     * 兼容 UpdateDTO 的下划线字段
     */
    private void copyUnderscoreFieldsToCamelCase(RequirementUpdateDTO dto) {
        // 兼容 project_id
        if (dto.getProjectId() == null && dto.getProject_id() != null) {
            dto.setProjectId(dto.getProject_id());
        }
        // 兼容 proposer_time
        if (dto.getProposerTime() == null && dto.getProposer_time() != null) {
            dto.setProposerTime(dto.getProposer_time());
        }
        // 兼容 effective_version
        if (dto.getEffectiveVersion() == null && dto.getEffective_version() != null) {
            dto.setEffectiveVersion(dto.getEffective_version());
        }
        // 兼容 acceptance_criteria
        if (dto.getAcceptanceCriteria() == null && dto.getAcceptance_criteria() != null) {
            dto.setAcceptanceCriteria(dto.getAcceptance_criteria());
        }
        // 兼容 permission_scope
        if (dto.getPermissionScope() == null && dto.getPermission_scope() != null) {
            dto.setPermissionScope(dto.getPermission_scope());
        }
        // 兼容 review_result
        if (dto.getReviewResult() == null && dto.getReview_result() != null) {
            dto.setReviewResult(dto.getReview_result());
        }
        // 兼容 review_comments
        if (dto.getReviewComments() == null && dto.getReview_comments() != null) {
            dto.setReviewComments(dto.getReview_comments());
        }
        // 兼容 online_time
        if (dto.getOnlineTime() == null && dto.getOnline_time() != null) {
            dto.setOnlineTime(dto.getOnline_time());
        }
        // 兼容 close_reason
        if (dto.getCloseReason() == null && dto.getClose_reason() != null) {
            dto.setCloseReason(dto.getClose_reason());
        }
    }

    /**
     * 创建需求
     */
    @Override
    public Requirement createRequirement(RequirementCreateDTO dto) {
        log.info("创建需求开始: title={}, projectId={}, type={}", 
            dto.getTitle(), dto.getProjectId(), dto.getType());
        
        // 先兼容下划线字段
        copyUnderscoreFieldsToCamelCase(dto);
        
        // 兼容前端name字段
        if (!StringUtils.hasText(dto.getTitle()) && StringUtils.hasText(dto.getName())) {
            dto.setTitle(dto.getName());
        }
        
        Requirement requirement = new Requirement();
        BeanUtils.copyProperties(dto, requirement);
        
        if (!StringUtils.hasText(requirement.getType())) {
            requirement.setType("functional");
        }
        if (!StringUtils.hasText(requirement.getPriority())) {
            requirement.setPriority("P2");
        }
        if (!StringUtils.hasText(requirement.getStatus())) {
            requirement.setStatus("draft");
        }
        if (!StringUtils.hasText(requirement.getSource())) {
            requirement.setSource("internal");
        }
        if (requirement.getAiAnalyzed() == null) {
            requirement.setAiAnalyzed(0);
        }
        
        requirement.setCreatedBy(1L);
        requirement.setCreateTime(LocalDateTime.now());
        requirement.setUpdateTime(LocalDateTime.now());
        
        log.info("保存需求: title={}, projectId={}", requirement.getTitle(), requirement.getProjectId());
        save(requirement);
        log.info("需求保存成功: id={}", requirement.getId());
        
        // 填充关联信息返回
        fillRequirementInfo(requirement);
        log.info("创建需求完成: id={}, creatorName={}, projectName={}", 
            requirement.getId(), requirement.getCreatorName(), requirement.getProjectName());
        return requirement;
    }

    /**
     * 更新需求
     */
    @Override
    public Requirement updateRequirement(RequirementUpdateDTO dto) {
        log.info("更新需求开始: id={}, title={}", dto.getId(), dto.getTitle());
        
        // 先兼容下划线字段
        copyUnderscoreFieldsToCamelCase(dto);
        
        // 兼容前端name字段
        if (!StringUtils.hasText(dto.getTitle()) && StringUtils.hasText(dto.getName())) {
            dto.setTitle(dto.getName());
        }
        
        Requirement requirement = getById(dto.getId());
        if (requirement == null) {
            log.warn("更新需求失败: 需求不存在, id={}", dto.getId());
            throw new RuntimeException("需求不存在");
        }
        
        // 逐个复制非空字段，避免覆盖原数据
        if (dto.getTitle() != null) requirement.setTitle(dto.getTitle());
        if (dto.getDescription() != null) requirement.setDescription(dto.getDescription());
        if (dto.getType() != null) requirement.setType(dto.getType());
        if (dto.getPriority() != null) requirement.setPriority(dto.getPriority());
        if (dto.getStatus() != null) requirement.setStatus(dto.getStatus());
        if (dto.getSource() != null) requirement.setSource(dto.getSource());
        if (dto.getProposer() != null) requirement.setProposer(dto.getProposer());
        if (dto.getProposerTime() != null) requirement.setProposerTime(dto.getProposerTime());
        if (dto.getEffectiveVersion() != null) requirement.setEffectiveVersion(dto.getEffectiveVersion());
        if (dto.getAcceptanceCriteria() != null) requirement.setAcceptanceCriteria(dto.getAcceptanceCriteria());
        if (dto.getOwner() != null) requirement.setOwner(dto.getOwner());
        if (dto.getReviewer() != null) requirement.setReviewer(dto.getReviewer());
        if (dto.getPermissionScope() != null) requirement.setPermissionScope(dto.getPermissionScope());
        if (dto.getReviewResult() != null) requirement.setReviewResult(dto.getReviewResult());
        if (dto.getReviewComments() != null) requirement.setReviewComments(dto.getReviewComments());
        if (dto.getOnlineTime() != null) requirement.setOnlineTime(dto.getOnlineTime());
        if (dto.getCloseReason() != null) requirement.setCloseReason(dto.getCloseReason());
        if (dto.getProjectId() != null) requirement.setProjectId(dto.getProjectId());
        if (dto.getDocumentUrl() != null) requirement.setDocumentUrl(dto.getDocumentUrl());
        if (dto.getAiAnalyzed() != null) requirement.setAiAnalyzed(dto.getAiAnalyzed());
        if (dto.getUpdatedBy() != null) requirement.setUpdatedBy(dto.getUpdatedBy());
        
        requirement.setUpdateTime(LocalDateTime.now());
        
        log.info("更新需求数据: id={}", dto.getId());
        updateById(requirement);
        log.info("需求更新成功: id={}", dto.getId());
        
        // 填充关联信息返回
        fillRequirementInfo(requirement);
        log.info("更新需求完成: id={}, creatorName={}, projectName={}", 
            requirement.getId(), requirement.getCreatorName(), requirement.getProjectName());
        return requirement;
    }

    /**
     * 删除需求
     */
    @Override
    public boolean deleteRequirement(Long id) {
        return removeById(id);
    }

    /**
     * 根据ID查询需求详情
     */
    @Override
    public Requirement getRequirementById(Long id) {
        Requirement requirement = getById(id);
        if (requirement != null) {
            fillRequirementInfo(requirement);
        }
        return requirement;
    }

    /**
     * 批量删除需求
     */
    @Override
    public boolean batchDeleteRequirements(List<Long> ids) {
        return removeByIds(ids);
    }

    /**
     * 分页查询需求列表
     */
    @Override
    public IPage<Requirement> queryRequirements(RequirementQueryDTO dto) {
        Page<Requirement> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        
        LambdaQueryWrapper<Requirement> wrapper = new LambdaQueryWrapper<>();
        
        if (dto.getProjectId() != null) {
            wrapper.eq(Requirement::getProjectId, dto.getProjectId());
        }
        
        if (StringUtils.hasText(dto.getKeyword())) {
            wrapper.and(w -> w.like(Requirement::getTitle, dto.getKeyword())
                    .or().like(Requirement::getDescription, dto.getKeyword()));
        }
        
        if (StringUtils.hasText(dto.getType())) {
            wrapper.eq(Requirement::getType, dto.getType());
        }
        
        if (StringUtils.hasText(dto.getPriority())) {
            wrapper.eq(Requirement::getPriority, dto.getPriority());
        }
        
        if (StringUtils.hasText(dto.getStatus())) {
            wrapper.eq(Requirement::getStatus, dto.getStatus());
        }
        
        wrapper.orderByDesc(Requirement::getCreateTime);
        
        IPage<Requirement> resultPage = page(page, wrapper);
        
        // 填充关联信息
        resultPage.getRecords().forEach(this::fillRequirementInfo);
        
        return resultPage;
    }

    /**
     * 查询项目下的所有需求
     */
    @Override
    public List<Requirement> getRequirementsByProjectId(Long projectId) {
        LambdaQueryWrapper<Requirement> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Requirement::getProjectId, projectId);
        wrapper.orderByDesc(Requirement::getCreateTime);
        return list(wrapper);
    }

    /**
     * 检查需求标题是否已存在
     */
    private boolean checkTitleExists(String title, Long projectId, Long excludeId) {
        LambdaQueryWrapper<Requirement> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Requirement::getTitle, title);
        wrapper.eq(Requirement::getProjectId, projectId);
        
        if (excludeId != null) {
            wrapper.ne(Requirement::getId, excludeId);
        }
        
        return count(wrapper) > 0;
    }
}
