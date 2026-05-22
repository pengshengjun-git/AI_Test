package com.aitest.project.dto;

import lombok.Data;

/**
 * 项目查询条件DTO
 */
@Data
public class ProjectQueryDTO {

    /**
     * 项目名称（模糊查询）
     */
    private String name;

    /**
     * 状态: PLANNING-规划中, IN_PROGRESS-进行中, COMPLETED-已完成, ARCHIVED-已归档
     */
    private String status;

    /**
     * 页码（兼容page和pageNum参数）
     */
    private Integer pageNum;
    private Integer page;
    private Integer pageSize;
    private Integer size;

    public Integer getPageNum() {
        if (pageNum != null) return pageNum;
        return page != null ? page : 1;
    }

    public Integer getPageSize() {
        if (pageSize != null) return pageSize;
        return size != null ? size : 10;
    }
}
