package com.aitest.testcase.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 测试用例创建DTO
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestcaseCreateDTO {

    /**
     * 项目ID
     */
    @NotNull(message = "项目ID不能为空")
    private Long projectId;

    /**
     * 关联需求ID
     */
    @NotNull(message = "需求ID不能为空")
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
     * 测试状态
     */
    private String testStatus;

    /**
     * 测试模块
     */
    private String testModule;

    /**
     * 测试步骤
     */
    private String steps;

    /**
     * 预期结果
     */
    private String expectedResult;

    /**
     * 标签
     */
    private String tags;

    /**
     * 创建人ID
     */
    private Long createdBy;
}
