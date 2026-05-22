package com.aitest.user.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 部门VO
 */
@Data
public class DepartmentVO {

    private Long id;
    private Long parentId;
    private String name;
    private String code;
    private Long leaderId;
    private String leaderName;
    private Integer sortOrder;
    private String description;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private List<DepartmentVO> children;
}
