package com.aitest.dashboard.service.impl;

import com.aitest.dashboard.dto.TestPlanCreateDTO;
import com.aitest.dashboard.dto.TestPlanQueryDTO;
import com.aitest.dashboard.entity.TestPlan;
import com.aitest.dashboard.mapper.TestPlanMapper;
import com.aitest.dashboard.service.TestPlanService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class TestPlanServiceImpl extends ServiceImpl<TestPlanMapper, TestPlan> implements TestPlanService {

    @Override
    public IPage<TestPlan> queryTestPlans(TestPlanQueryDTO dto) {
        Page<TestPlan> page = new Page<>(dto.getPage(), dto.getSize());
        LambdaQueryWrapper<TestPlan> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(dto.getName())) {
            wrapper.like(TestPlan::getName, dto.getName());
        }
        if (StringUtils.hasText(dto.getStatus())) {
            wrapper.eq(TestPlan::getStatus, dto.getStatus());
        }
        if (dto.getProjectId() != null) {
            wrapper.eq(TestPlan::getProjectId, dto.getProjectId());
        }
        wrapper.orderByDesc(TestPlan::getCreateTime);
        return this.page(page, wrapper);
    }

    @Override
    public TestPlan getTestPlanById(Long id) {
        return this.getById(id);
    }

    @Override
    public TestPlan createTestPlan(TestPlanCreateDTO dto) {
        TestPlan testPlan = new TestPlan();
        BeanUtils.copyProperties(dto, testPlan);
        if (!StringUtils.hasText(testPlan.getStatus())) {
            testPlan.setStatus("draft");
        }
        testPlan.setCreatedBy(1L);
        this.save(testPlan);
        return testPlan;
    }

    @Override
    public TestPlan updateTestPlan(Long id, TestPlanCreateDTO dto) {
        TestPlan testPlan = this.getById(id);
        if (testPlan == null) {
            return null;
        }
        if (dto.getName() != null) testPlan.setName(dto.getName());
        if (dto.getDescription() != null) testPlan.setDescription(dto.getDescription());
        if (dto.getProjectId() != null) testPlan.setProjectId(dto.getProjectId());
        if (dto.getStartDate() != null) testPlan.setStartDate(dto.getStartDate());
        if (dto.getEndDate() != null) testPlan.setEndDate(dto.getEndDate());
        if (dto.getStatus() != null) testPlan.setStatus(dto.getStatus());
        if (dto.getOwner() != null) testPlan.setOwner(dto.getOwner());
        testPlan.setUpdatedBy(1L);
        this.updateById(testPlan);
        return testPlan;
    }

    @Override
    public boolean deleteTestPlan(Long id) {
        return this.removeById(id);
    }

    @Override
    public List<TestPlan> getTestPlansByProjectId(Long projectId) {
        LambdaQueryWrapper<TestPlan> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TestPlan::getProjectId, projectId);
        return this.list(wrapper);
    }
}
