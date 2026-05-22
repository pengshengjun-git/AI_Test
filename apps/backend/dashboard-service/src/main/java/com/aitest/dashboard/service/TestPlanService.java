package com.aitest.dashboard.service;

import com.aitest.dashboard.dto.TestPlanCreateDTO;
import com.aitest.dashboard.dto.TestPlanQueryDTO;
import com.aitest.dashboard.entity.TestPlan;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

public interface TestPlanService {
    IPage<TestPlan> queryTestPlans(TestPlanQueryDTO dto);
    TestPlan getTestPlanById(Long id);
    TestPlan createTestPlan(TestPlanCreateDTO dto);
    TestPlan updateTestPlan(Long id, TestPlanCreateDTO dto);
    boolean deleteTestPlan(Long id);
    List<TestPlan> getTestPlansByProjectId(Long projectId);
}
