package com.aitest.testcase.dto;

import lombok.Data;

/**
 * 测试用例查询DTO
 */
@Data
public class TestcaseQueryDTO {

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 关联需求ID
     */
    private Long requirementId;

    /**
     * 用例标题关键词
     */
    private String keyword;

    /**
     * 用例类型
     */
    private String type;

    /**
     * 优先级
     */
    private String priority;

    /**
     * 状态
     */
    private String status;

    /**
     * 标签
     */
    private String tag;

    /**
     * 是否AI生成
     */
    private Integer aiGenerated;

    /**
     * 页码
     */
    private Integer pageNum = 1;

    /**
     * 每页大小
     */
    private Integer pageSize = 10;
}
