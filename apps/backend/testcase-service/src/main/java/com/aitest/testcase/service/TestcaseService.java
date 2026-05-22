package com.aitest.testcase.service;

import com.aitest.testcase.dto.TestcaseCreateDTO;
import com.aitest.testcase.dto.TestcaseQueryDTO;
import com.aitest.testcase.dto.TestcaseUpdateDTO;
import com.aitest.testcase.entity.Testcase;
import com.aitest.testcase.entity.TestcaseStep;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 测试用例Service接口
 */
public interface TestcaseService extends IService<Testcase> {

    /**
     * 创建测试用例
     */
    Testcase createTestcase(TestcaseCreateDTO dto);

    /**
     * 更新测试用例
     */
    Testcase updateTestcase(TestcaseUpdateDTO dto);

    /**
     * 删除测试用例
     */
    boolean deleteTestcase(Long id);

    /**
     * 根据ID查询测试用例详情（包含步骤）
     */
    Testcase getTestcaseDetail(Long id);

    /**
     * 分页查询测试用例列表
     */
    IPage<Testcase> queryTestcases(TestcaseQueryDTO dto);

    /**
     * 查询项目下的所有测试用例
     */
    List<Testcase> getTestcasesByProjectId(Long projectId);

    /**
     * 查询需求下的所有测试用例
     */
    List<Testcase> getTestcasesByRequirementId(Long requirementId);

    /**
     * 批量删除测试用例
     */
    boolean batchDeleteTestcases(List<Long> ids);

    /**
     * 获取用例步骤列表
     */
    List<TestcaseStep> getTestcaseSteps(Long testcaseId);

    /**
     * 批量添加标签
     */
    boolean addTags(Long testcaseId, List<String> tags);

    /**
     * 移除标签
     */
    boolean removeTag(Long testcaseId, String tag);
}
