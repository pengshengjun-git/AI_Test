package com.aitest.testcase.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用例步骤实体类
 */
@Data
@TableName("testcase_step")
public class TestcaseStep implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 步骤ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用例ID
     */
    private Long testcaseId;

    /**
     * 步骤序号
     */
    private Integer stepNumber;

    /**
     * 操作步骤
     */
    private String action;

    /**
     * 预期结果
     */
    private String expectedResult;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}
