package com.aitest.project.dto;

import lombok.Data;

/**
 * 项目成员操作DTO
 */
@Data
public class ProjectMemberDTO {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 项目角色: owner, manager, member, viewer
     */
    private String role;
}
