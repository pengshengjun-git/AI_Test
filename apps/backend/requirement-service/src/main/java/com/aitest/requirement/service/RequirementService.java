package com.aitest.requirement.service;

import com.aitest.requirement.dto.RequirementCreateDTO;
import com.aitest.requirement.dto.RequirementQueryDTO;
import com.aitest.requirement.dto.RequirementUpdateDTO;
import com.aitest.requirement.entity.Requirement;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 需求Service接口
 */
public interface RequirementService extends IService<Requirement> {

    /**
     * 创建需求
     */
    Requirement createRequirement(RequirementCreateDTO dto);

    /**
     * 更新需求
     */
    Requirement updateRequirement(RequirementUpdateDTO dto);

    /**
     * 删除需求
     */
    boolean deleteRequirement(Long id);

    /**
     * 根据ID查询需求详情
     */
    Requirement getRequirementById(Long id);

    /**
     * 分页查询需求列表
     */
    IPage<Requirement> queryRequirements(RequirementQueryDTO dto);

    /**
     * 查询项目下的所有需求
     */
    List<Requirement> getRequirementsByProjectId(Long projectId);

    /**
     * 批量删除需求
     */
    boolean batchDeleteRequirements(List<Long> ids);
}
