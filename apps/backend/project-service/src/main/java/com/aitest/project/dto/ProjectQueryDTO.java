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
     * 状态: active-活跃, archived-归档
     */
    private String status;

    /**
     * 可见性: private-私有, public-公开
     */
    private String visibility;

    /**
     * 负责人ID
     */
    private Long ownerId;

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
