package com.aitest.testcase.service.impl;

import com.aitest.testcase.dto.TestcaseCreateDTO;
import com.aitest.testcase.dto.TestcaseQueryDTO;
import com.aitest.testcase.dto.TestcaseUpdateDTO;
import com.aitest.testcase.entity.Testcase;
import com.aitest.testcase.entity.TestcaseStep;
import com.aitest.testcase.mapper.TestcaseMapper;
import com.aitest.testcase.mapper.TestcaseStepMapper;
import com.aitest.testcase.service.TestcaseService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 测试用例服务实现
 */
@Service
public class TestcaseServiceImpl extends ServiceImpl<TestcaseMapper, Testcase> implements TestcaseService {

    @Autowired
    private TestcaseStepMapper testcaseStepMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Testcase createTestcase(TestcaseCreateDTO dto) {
        // 创建用例
        Testcase testcase = new Testcase();
        BeanUtils.copyProperties(dto, testcase);
        testcase.setCreatedBy(1L); // 默认创建者ID
        testcase.setCreateTime(java.time.LocalDateTime.now());
        testcase.setUpdateTime(java.time.LocalDateTime.now());
        this.save(testcase);

        return testcase;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Testcase updateTestcase(TestcaseUpdateDTO dto) {
        // 更新用例
        Testcase testcase = this.getById(dto.getId());
        if (testcase == null) {
            return null;
        }
        BeanUtils.copyProperties(dto, testcase);
        testcase.setUpdateTime(java.time.LocalDateTime.now());
        this.updateById(testcase);

        return testcase;
    }

    @Override
    public Testcase getTestcaseDetail(Long id) {
        // 获取用例信息
        Testcase testcase = this.getById(id);
        if (testcase == null) {
            return null;
        }

        // 获取用例步骤
        LambdaQueryWrapper<TestcaseStep> stepWrapper = new LambdaQueryWrapper<>();
        stepWrapper.eq(TestcaseStep::getTestcaseId, id);
        stepWrapper.orderByAsc(TestcaseStep::getStepNumber);
        List<TestcaseStep> steps = testcaseStepMapper.selectList(stepWrapper);

        // 这里Testcase实体没有steps字段，我们就不设置了
        // 实际项目中可以创建VO类来包含步骤信息

        return testcase;
    }

    @Override
    public Page<Testcase> queryTestcases(TestcaseQueryDTO dto) {
        Page<Testcase> page = new Page<>(dto.getPageNum(), dto.getPageSize());

        LambdaQueryWrapper<Testcase> wrapper = new LambdaQueryWrapper<>();

        // 项目ID
        if (dto.getProjectId() != null) {
            wrapper.eq(Testcase::getProjectId, dto.getProjectId());
        }

        // 需求ID
        if (dto.getRequirementId() != null) {
            wrapper.eq(Testcase::getRequirementId, dto.getRequirementId());
        }

        // 关键词（标题）
        if (dto.getKeyword() != null && !dto.getKeyword().isEmpty()) {
            wrapper.like(Testcase::getTitle, dto.getKeyword());
        }

        // 类型
        if (dto.getType() != null && !dto.getType().isEmpty()) {
            wrapper.eq(Testcase::getType, dto.getType());
        }

        // 优先级
        if (dto.getPriority() != null && !dto.getPriority().isEmpty()) {
            wrapper.eq(Testcase::getPriority, dto.getPriority());
        }

        // 状态
        if (dto.getStatus() != null && !dto.getStatus().isEmpty()) {
            wrapper.eq(Testcase::getStatus, dto.getStatus());
        }

        // 标签
        if (dto.getTag() != null && !dto.getTag().isEmpty()) {
            wrapper.like(Testcase::getTags, dto.getTag());
        }

        // AI生成
        if (dto.getAiGenerated() != null) {
            wrapper.eq(Testcase::getAiGenerated, dto.getAiGenerated());
        }

        wrapper.orderByDesc(Testcase::getCreateTime);

        return this.page(page, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteTestcase(Long id) {
        // 删除用例步骤
        LambdaQueryWrapper<TestcaseStep> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(TestcaseStep::getTestcaseId, id);
        testcaseStepMapper.delete(deleteWrapper);

        // 删除用例
        return this.removeById(id);
    }

    @Override
    public List<TestcaseStep> getTestcaseSteps(Long id) {
        LambdaQueryWrapper<TestcaseStep> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TestcaseStep::getTestcaseId, id);
        wrapper.orderByAsc(TestcaseStep::getStepNumber);
        return testcaseStepMapper.selectList(wrapper);
    }

    @Override
    public boolean addTags(Long testcaseId, List<String> tags) {
        Testcase testcase = this.getById(testcaseId);
        if (testcase == null) {
            return false;
        }
        String existingTags = testcase.getTags();
        if (existingTags == null) {
            existingTags = String.join(",", tags);
        } else {
            for (String tag : tags) {
                if (!existingTags.contains(tag)) {
                    existingTags += "," + tag;
                }
            }
        }
        testcase.setTags(existingTags);
        return this.updateById(testcase);
    }

    @Override
    public boolean removeTag(Long testcaseId, String tag) {
        Testcase testcase = this.getById(testcaseId);
        if (testcase == null) {
            return false;
        }
        String existingTags = testcase.getTags();
        if (existingTags == null || existingTags.isEmpty()) {
            return true;
        }
        existingTags = existingTags.replace(tag, "").replace(",,", ",");
        if (existingTags.endsWith(",")) {
            existingTags = existingTags.substring(0, existingTags.length() - 1);
        }
        if (existingTags.startsWith(",")) {
            existingTags = existingTags.substring(1);
        }
        testcase.setTags(existingTags);
        return this.updateById(testcase);
    }

    @Override
    public List<Testcase> getTestcasesByProjectId(Long projectId) {
        LambdaQueryWrapper<Testcase> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Testcase::getProjectId, projectId);
        wrapper.orderByDesc(Testcase::getCreateTime);
        return this.list(wrapper);
    }

    @Override
    public List<Testcase> getTestcasesByRequirementId(Long requirementId) {
        LambdaQueryWrapper<Testcase> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Testcase::getRequirementId, requirementId);
        wrapper.orderByDesc(Testcase::getCreateTime);
        return this.list(wrapper);
    }

    @Override
    public boolean batchDeleteTestcases(List<Long> ids) {
        return this.removeByIds(ids);
    }
}
