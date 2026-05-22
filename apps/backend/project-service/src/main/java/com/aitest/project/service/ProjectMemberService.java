package com.aitest.project.service;

import com.aitest.project.dto.ProjectMemberDTO;
import com.aitest.project.entity.ProjectMember;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 项目成员Service接口
 */
public interface ProjectMemberService extends IService<ProjectMember> {

    /**
     * 获取项目成员列表
     */
    List<ProjectMember> getMembersByProjectId(Long projectId);

    /**
     * 添加项目成员
     */
    ProjectMember addMember(Long projectId, ProjectMemberDTO memberDTO, Long operatorId);

    /**
     * 批量添加项目成员
     */
    List<ProjectMember> addMembers(Long projectId, List<ProjectMemberDTO> memberDTOList, Long operatorId);

    /**
     * 更新项目成员角色
     */
    ProjectMember updateMemberRole(Long projectId, Long userId, String role);

    /**
     * 移除项目成员
     */
    boolean removeMember(Long projectId, Long userId);

    /**
     * 检查用户是否为项目成员
     */
    boolean isMember(Long projectId, Long userId);

    /**
     * 获取用户在项目中的角色
     */
    String getUserRole(Long projectId, Long userId);
}
