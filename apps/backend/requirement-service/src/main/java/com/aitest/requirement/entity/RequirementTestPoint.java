package com.aitest.requirement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 需求测试点实体类
 */
@Data
@TableName("requirement_test_point")
public class RequirementTestPoint implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 需求ID
     */
    private Long requirementId;

    /**
     * 测试点内容
     */
    private String content;

    /**
     * 类型: function-功能, boundary-边界, exception-异常, security-安全
     */
    private String type;

    /**
     * 是否AI生成: 0-否, 1-是
     */
    private Integer aiGenerated;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}
