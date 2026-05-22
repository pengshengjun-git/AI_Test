package com.aitest.requirement.service.impl;

import com.aitest.requirement.dto.RequirementCreateDTO;
import com.aitest.requirement.dto.RequirementQueryDTO;
import com.aitest.requirement.dto.RequirementUpdateDTO;
import com.aitest.requirement.entity.Requirement;
import com.aitest.requirement.mapper.RequirementMapper;
import com.aitest.requirement.service.RequirementService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 需求Service实现类
 */
@Service
public class RequirementServiceImpl extends ServiceImpl<RequirementMapper, Requirement> implements RequirementService {

    /**
     * 创建需求
     */
    @Override
    public Requirement createRequirement(RequirementCreateDTO dto) {
        // 检查需求标题是否已存在（同一项目下）
        if (checkTitleExists(dto.getTitle(), dto.getProjectId(), null)) {
            throw new RuntimeException("需求标题已存在");
        }
        
        Requirement requirement = new Requirement();
        BeanUtils.copyProperties(dto, requirement);
        
        // 设置默认值
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
        requirement.setCreatorId(1L); // 默认创建者ID
        requirement.setCreateTime(java.time.LocalDateTime.now());
        requirement.setUpdateTime(java.time.LocalDateTime.now());
        
        save(requirement);
        return requirement;
    }

    /**
     * 更新需求
     */
    @Override
    public Requirement updateRequirement(RequirementUpdateDTO dto) {
        Requirement requirement = getById(dto.getId());
        if (requirement == null) {
            throw new RuntimeException("需求不存在");
        }
        
        BeanUtils.copyProperties(dto, requirement);
        requirement.setUpdateTime(java.time.LocalDateTime.now());
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
     * 分页查询需求列表
     */
    @Override
    public IPage<Requirement> queryRequirements(RequirementQueryDTO dto) {
        Page<Requirement> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        
        LambdaQueryWrapper<Requirement> wrapper = new LambdaQueryWrapper<>();
        
        // 项目ID
        if (dto.getProjectId() != null) {
            wrapper.eq(Requirement::getProjectId, dto.getProjectId());
        }
        
        // 关键词搜索
        if (StringUtils.hasText(dto.getKeyword())) {
            wrapper.and(w -> w.like(Requirement::getTitle, dto.getKeyword())
                    .or().like(Requirement::getDescription, dto.getKeyword()));
        }
        
        // 类型
        if (StringUtils.hasText(dto.getType())) {
            wrapper.eq(Requirement::getType, dto.getType());
        }
        
        // 优先级
        if (StringUtils.hasText(dto.getPriority())) {
            wrapper.eq(Requirement::getPriority, dto.getPriority());
        }
        
        // 状态
        if (StringUtils.hasText(dto.getStatus())) {
            wrapper.eq(Requirement::getStatus, dto.getStatus());
        }
        
        // 按创建时间倒序
        wrapper.orderByDesc(Requirement::getCreateTime);
        
        return page(page, wrapper);
    }

    /**
     * 查询项目下的所有需求
     */
    @Override
    public List<Requirement> getRequirementsByProjectId(Long projectId) {
        LambdaQueryWrapper<Requirement> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Requirement::getProjectId, projectId)
                .orderByDesc(Requirement::getCreateTime);
        return list(wrapper);
    }

    /**
     * 批量删除需求
     */
    @Override
    public boolean batchDeleteRequirements(List<Long> ids) {
        return removeByIds(ids);
    }
    
    /**
     * 检查需求标题是否存在（同一项目下）
     * @param title 需求标题
     * @param projectId 项目ID
     * @param excludeId 排除的需求ID（用于更新时排除自身）
     * @return 是否存在
     */
    private boolean checkTitleExists(String title, Long projectId, Long excludeId) {
        QueryWrapper<Requirement> wrapper = new QueryWrapper<>();
        wrapper.eq("title", title)
               .eq("project_id", projectId);
        
        if (excludeId != null) {
            wrapper.ne("id", excludeId);
        }
        
        return this.count(wrapper) > 0;
    }
}
