package com.aitest.project.service.impl;

import com.aitest.project.dto.ProjectMemberDTO;
import com.aitest.project.entity.ProjectMember;
import com.aitest.project.mapper.ProjectMemberMapper;
import com.aitest.project.service.ProjectMemberService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目成员Service实现类
 */
@Service
public class ProjectMemberServiceImpl extends ServiceImpl<ProjectMemberMapper, ProjectMember> implements ProjectMemberService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectMemberServiceImpl.class);

    @Override
    public List<ProjectMember> getMembersByProjectId(Long projectId) {
        LambdaQueryWrapper<ProjectMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProjectMember::getProjectId, projectId);
        wrapper.orderByAsc(ProjectMember::getJoinedAt);
        return this.list(wrapper);
    }

    @Override
    public ProjectMember addMember(Long projectId, ProjectMemberDTO memberDTO, Long operatorId) {
        // 检查是否已存在
        if (isMember(projectId, memberDTO.getUserId())) {
            throw new RuntimeException("该用户已在项目中");
        }
        
        ProjectMember member = new ProjectMember();
        member.setProjectId(projectId);
        member.setUserId(memberDTO.getUserId());
        member.setRole(memberDTO.getRole() != null ? memberDTO.getRole() : "member");
        member.setJoinedAt(LocalDateTime.now());
        member.setCreatedBy(operatorId);
        
        this.save(member);
        LOGGER.info("添加项目成员成功: projectId={}, userId={}", projectId, memberDTO.getUserId());
        return member;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ProjectMember> addMembers(Long projectId, List<ProjectMemberDTO> memberDTOList, Long operatorId) {
        List<ProjectMember> members = new ArrayList<>();
        for (ProjectMemberDTO dto : memberDTOList) {
            members.add(addMember(projectId, dto, operatorId));
        }
        return members;
    }

    @Override
    public ProjectMember updateMemberRole(Long projectId, Long userId, String role) {
        LambdaQueryWrapper<ProjectMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProjectMember::getProjectId, projectId);
        wrapper.eq(ProjectMember::getUserId, userId);
        
        ProjectMember member = this.getOne(wrapper);
        if (member == null) {
            throw new RuntimeException("项目成员不存在");
        }
        
        member.setRole(role);
        this.updateById(member);
        
        LOGGER.info("更新项目成员角色成功: projectId={}, userId={}, role={}", projectId, userId, role);
        return member;
    }

    @Override
    public boolean removeMember(Long projectId, Long userId) {
        LambdaQueryWrapper<ProjectMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProjectMember::getProjectId, projectId);
        wrapper.eq(ProjectMember::getUserId, userId);
        
        boolean result = this.remove(wrapper);
        LOGGER.info("移除项目成员成功: projectId={}, userId={}", projectId, userId);
        return result;
    }

    @Override
    public boolean isMember(Long projectId, Long userId) {
        LambdaQueryWrapper<ProjectMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProjectMember::getProjectId, projectId);
        wrapper.eq(ProjectMember::getUserId, userId);
        return this.count(wrapper) > 0;
    }

    @Override
    public String getUserRole(Long projectId, Long userId) {
        LambdaQueryWrapper<ProjectMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProjectMember::getProjectId, projectId);
        wrapper.eq(ProjectMember::getUserId, userId);
        ProjectMember member = this.getOne(wrapper);
        return member != null ? member.getRole() : null;
    }
}
