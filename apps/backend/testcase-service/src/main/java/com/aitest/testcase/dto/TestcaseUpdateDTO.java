package com.aitest.testcase.dto;

import com.aitest.testcase.entity.TestcaseStep;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * 测试用例更新DTO
 */
@Data
public class TestcaseUpdateDTO {

    /**
     * 用例ID
     */
    private Long id;

    /**
     * 关联需求ID
     */
    private Long requirementId;

    /**
     * 用例标题
     */
    @NotBlank(message = "用例标题不能为空")
    private String title;

    /**
     * 用例描述
     */
    private String description;

    /**
     * 前置条件
     */
    private String preconditions;

    /**
     * 优先级
     */
    private String priority;

    /**
     * 用例类型
     */
    private String type;

    /**
     * 状态
     */
    private String status;

    /**
     * 标签
     */
    private String tags;

    /**
     * 用例步骤
     */
    private List<TestcaseStep> steps;

    /**
     * 更新人ID
     */
    private Long updatedBy;
}
