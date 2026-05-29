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
     * 创建需求
     */
    @Override
    public Requirement createRequirement(RequirementCreateDTO dto) {
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
            requirement.setSource("manual");
        }
        if (requirement.getAiAnalyzed() == null) {
            requirement.setAiAnalyzed(0);
        }
        
        requirement.setCreatedBy(1L);
        requirement.setCreateTime(LocalDateTime.now());
        requirement.setUpdateTime(LocalDateTime.now());
        
        save(requirement);
        return requirement;
    }

    /**
     * 更新需求
     */
    @Override
    public Requirement updateRequirement(RequirementUpdateDTO dto) {
        // 兼容前端name字段
        if (!StringUtils.hasText(dto.getTitle()) && StringUtils.hasText(dto.getName())) {
            dto.setTitle(dto.getName());
        }
        
        Requirement requirement = getById(dto.getId());
        if (requirement == null) {
            throw new RuntimeException("需求不存在");
        }
        
        BeanUtils.copyProperties(dto, requirement);
        requirement.setUpdateTime(LocalDateTime.now());
        
        updateById(requirement);
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
        return getById(id);
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
        
        // 填充创建人用户名
        resultPage.getRecords().forEach(requirement -> {
            requirement.setCreatorName(getUserName(requirement.getCreatedBy()));
        });
        
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
