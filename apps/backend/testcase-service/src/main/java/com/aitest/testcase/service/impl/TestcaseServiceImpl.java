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
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 测试用例服务实现
 */
@Slf4j
@Service
public class TestcaseServiceImpl extends ServiceImpl<TestcaseMapper, Testcase> implements TestcaseService {

    @Autowired
    private TestcaseStepMapper testcaseStepMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String PROJECT_SERVICE_URL = "http://localhost:9003/api/v1/projects";
    private static final String REQUIREMENT_SERVICE_URL = "http://localhost:9004/api/v1/requirements";

    /**
     * 获取项目名称
     */
    private String getProjectName(Long projectId) {
        if (projectId == null) {
            return null;
        }
        try {
            String url = PROJECT_SERVICE_URL + "/" + projectId;
            String response = restTemplate.getForObject(url, String.class);
            Map<String, Object> result = objectMapper.readValue(response, new TypeReference<Map<String, Object>>() {});
            Map<String, Object> data = (Map<String, Object>) result.get("data");
            if (data != null) {
                return (String) data.get("name");
            }
        } catch (Exception e) {
            log.warn("获取项目名称失败: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 获取需求标题
     */
    private String getRequirementTitle(Long requirementId) {
        if (requirementId == null) {
            return null;
        }
        try {
            String url = REQUIREMENT_SERVICE_URL + "/" + requirementId;
            String response = restTemplate.getForObject(url, String.class);
            Map<String, Object> result = objectMapper.readValue(response, new TypeReference<Map<String, Object>>() {});
            Map<String, Object> data = (Map<String, Object>) result.get("data");
            if (data != null) {
                String title = (String) data.get("title");
                if (title == null) {
                    title = (String) data.get("name");
                }
                return title;
            }
        } catch (Exception e) {
            log.warn("获取需求标题失败: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 填充测试用例的项目名称和需求标题
     */
    private void fillProjectAndRequirementInfo(Testcase testcase) {
        if (testcase == null) {
            return;
        }
        testcase.setProjectName(getProjectName(testcase.getProjectId()));
        testcase.setRequirementTitle(getRequirementTitle(testcase.getRequirementId()));
    }

    /**
     * 批量填充测试用例的项目名称和需求标题
     */
    private void fillProjectAndRequirementInfo(List<Testcase> testcases) {
        if (testcases == null || testcases.isEmpty()) {
            return;
        }
        for (Testcase testcase : testcases) {
            fillProjectAndRequirementInfo(testcase);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Testcase createTestcase(TestcaseCreateDTO dto) {
        Testcase testcase = new Testcase();
        BeanUtils.copyProperties(dto, testcase);
        testcase.setCreatedBy(1L);
        testcase.setCreateTime(java.time.LocalDateTime.now());
        testcase.setUpdateTime(java.time.LocalDateTime.now());
        this.save(testcase);

        return testcase;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Testcase updateTestcase(TestcaseUpdateDTO dto) {
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
        Testcase testcase = this.getById(id);
        if (testcase == null) {
            return null;
        }

        LambdaQueryWrapper<TestcaseStep> stepWrapper = new LambdaQueryWrapper<>();
        stepWrapper.eq(TestcaseStep::getTestcaseId, id);
        stepWrapper.orderByAsc(TestcaseStep::getStepNumber);
        List<TestcaseStep> steps = testcaseStepMapper.selectList(stepWrapper);

        return testcase;
    }

    @Override
    public Page<Testcase> queryTestcases(TestcaseQueryDTO dto) {
        Page<Testcase> page = new Page<>(dto.getPageNum(), dto.getPageSize());

        LambdaQueryWrapper<Testcase> wrapper = new LambdaQueryWrapper<>();

        if (dto.getProjectId() != null) {
            wrapper.eq(Testcase::getProjectId, dto.getProjectId());
        }

        if (dto.getRequirementId() != null) {
            wrapper.eq(Testcase::getRequirementId, dto.getRequirementId());
        }

        if (dto.getKeyword() != null && !dto.getKeyword().isEmpty()) {
            wrapper.like(Testcase::getTitle, dto.getKeyword());
        }

        if (dto.getType() != null && !dto.getType().isEmpty()) {
            wrapper.eq(Testcase::getType, dto.getType());
        }

        if (dto.getPriority() != null && !dto.getPriority().isEmpty()) {
            wrapper.eq(Testcase::getPriority, dto.getPriority());
        }

        if (dto.getStatus() != null && !dto.getStatus().isEmpty()) {
            wrapper.eq(Testcase::getStatus, dto.getStatus());
        }

        if (dto.getTag() != null && !dto.getTag().isEmpty()) {
            wrapper.like(Testcase::getTags, dto.getTag());
        }

        if (dto.getAiGenerated() != null) {
            wrapper.eq(Testcase::getAiGenerated, dto.getAiGenerated());
        }

        wrapper.orderByDesc(Testcase::getCreateTime);

        Page<Testcase> resultPage = this.page(page, wrapper);
        // 填充项目名称和需求标题
        fillProjectAndRequirementInfo(resultPage.getRecords());
        return resultPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteTestcase(Long id) {
        LambdaQueryWrapper<TestcaseStep> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(TestcaseStep::getTestcaseId, id);
        testcaseStepMapper.delete(deleteWrapper);

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

    @Override
    public Map<String, Object> getStatistics(Long projectId) {
        Map<String, Object> stats = new HashMap<>();
        
        if (projectId != null) {
            // 统计所有属于该项目的测试用例：直接关联 + 通过需求关联
            LambdaQueryWrapper<Testcase> wrapper = new LambdaQueryWrapper<>();
            wrapper.and(w -> w.eq(Testcase::getProjectId, projectId)
                    .or()
                    .exists("SELECT 1 FROM requirement r WHERE r.id = testcase.requirement_id AND r.project_id = {0}", projectId));
            stats.put("total", this.count(wrapper));
            
            LambdaQueryWrapper<Testcase> aiWrapper = new LambdaQueryWrapper<>();
            aiWrapper.and(w -> w.eq(Testcase::getProjectId, projectId)
                    .or()
                    .exists("SELECT 1 FROM requirement r WHERE r.id = testcase.requirement_id AND r.project_id = {0}", projectId));
            aiWrapper.eq(Testcase::getAiGenerated, true);
            stats.put("aiGenerated", this.count(aiWrapper));
        } else {
            stats.put("total", this.count());
            LambdaQueryWrapper<Testcase> aiWrapper = new LambdaQueryWrapper<>();
            aiWrapper.eq(Testcase::getAiGenerated, true);
            stats.put("aiGenerated", this.count(aiWrapper));
        }
        
        return stats;
    }
}
