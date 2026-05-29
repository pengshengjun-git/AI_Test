package com.aitest.defect.service.impl;

import com.aitest.defect.dto.DefectCreateDTO;
import com.aitest.defect.dto.DefectQueryDTO;
import com.aitest.defect.entity.Defect;
import com.aitest.defect.mapper.DefectMapper;
import com.aitest.defect.service.DefectService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 缺陷服务实现
 */
@Slf4j
@Service
public class DefectServiceImpl extends ServiceImpl<DefectMapper, Defect> implements DefectService {

    private final JdbcTemplate jdbcTemplate;

    public DefectServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private void fillDefectInfo(Defect defect) {
        if (defect == null) return;
        defect.setProjectName(getProjectName(defect.getProjectId()));
        defect.setRequirementName(getRequirementTitle(defect.getRequirementId()));
        defect.setHandler(getUserName(defect.getAssignee()));
        defect.setReporterName(getUserName(defect.getReporterId()));
    }

    private String getProjectName(Long projectId) {
        if (projectId == null) return null;
        try {
            String sql = "SELECT name FROM project WHERE id = ? AND deleted = 0";
            return jdbcTemplate.queryForObject(sql, String.class, projectId);
        } catch (Exception e) {
            log.warn("获取项目名称失败: projectId={}", projectId);
            return null;
        }
    }

    private String getRequirementTitle(Long requirementId) {
        if (requirementId == null) return null;
        try {
            String sql = "SELECT title FROM requirement WHERE id = ? AND deleted = 0";
            return jdbcTemplate.queryForObject(sql, String.class, requirementId);
        } catch (Exception e) {
            log.warn("获取需求标题失败: requirementId={}", requirementId);
            return null;
        }
    }

    private String getUserName(Long userId) {
        if (userId == null) return null;
        try {
            String sql = "SELECT username FROM `user` WHERE id = ? AND deleted = 0";
            return jdbcTemplate.queryForObject(sql, String.class, userId);
        } catch (Exception e) {
            log.warn("获取用户名失败: userId={}", userId);
            return null;
        }
    }

    @Override
    public Defect createDefect(DefectCreateDTO dto) {
        Defect defect = new Defect();
        BeanUtils.copyProperties(dto, defect);
        if (dto.getSteps() != null) {
            defect.setStepsToReproduce(dto.getSteps());
        }
        defect.setCreatedBy(1L);
        this.save(defect);
        fillDefectInfo(defect);
        return defect;
    }

    @Override
    public Defect updateDefect(Long id, DefectCreateDTO dto) {
        Defect defect = this.getById(id);
        if (defect == null) {
            return null;
        }
        if (dto.getTitle() != null) defect.setTitle(dto.getTitle());
        if (dto.getDescription() != null) defect.setDescription(dto.getDescription());
        if (dto.getPriority() != null) defect.setPriority(dto.getPriority());
        if (dto.getStatus() != null) defect.setStatus(dto.getStatus());
        if (dto.getType() != null) defect.setType(dto.getType());
        if (dto.getSeverity() != null) defect.setSeverity(dto.getSeverity());
        if (dto.getModule() != null) defect.setModule(dto.getModule());
        if (dto.getSteps() != null) defect.setStepsToReproduce(dto.getSteps());
        if (dto.getExpectedResult() != null) defect.setExpectedResult(dto.getExpectedResult());
        if (dto.getActualResult() != null) defect.setActualResult(dto.getActualResult());
        if (dto.getAssignee() != null) defect.setAssignee(dto.getAssignee());
        defect.setUpdatedBy(1L);
        this.updateById(defect);
        fillDefectInfo(defect);
        return defect;
    }

    @Override
    public boolean deleteDefect(Long id) {
        return this.removeById(id);
    }

    @Override
    public boolean batchDeleteDefects(List<Long> ids) {
        return this.removeByIds(ids);
    }

    @Override
    public Defect getDefectById(Long id) {
        Defect defect = this.getById(id);
        fillDefectInfo(defect);
        return defect;
    }

    @Override
    public IPage<Defect> queryDefects(DefectQueryDTO dto) {
        Page<Defect> page = new Page<>(dto.getPage(), dto.getSize());
        LambdaQueryWrapper<Defect> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(dto.getTitle())) {
            wrapper.like(Defect::getTitle, dto.getTitle());
        }
        if (StringUtils.hasText(dto.getPriority())) {
            wrapper.eq(Defect::getPriority, dto.getPriority());
        }
        if (StringUtils.hasText(dto.getStatus())) {
            wrapper.eq(Defect::getStatus, dto.getStatus());
        }
        if (StringUtils.hasText(dto.getSeverity())) {
            wrapper.eq(Defect::getSeverity, dto.getSeverity());
        }
        if (dto.getProjectId() != null) {
            wrapper.eq(Defect::getProjectId, dto.getProjectId());
        }
        if (dto.getAssignee() != null) {
            wrapper.eq(Defect::getAssignee, dto.getAssignee());
        }
        wrapper.orderByDesc(Defect::getCreateTime);
        IPage<Defect> result = this.page(page, wrapper);
        result.getRecords().forEach(this::fillDefectInfo);
        return result;
    }

    @Override
    public List<Defect> getDefectsByProjectId(Long projectId) {
        LambdaQueryWrapper<Defect> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Defect::getProjectId, projectId);
        List<Defect> list = this.list(wrapper);
        list.forEach(this::fillDefectInfo);
        return list;
    }

    @Override
    public Defect assignDefect(Long id, Long assignee) {
        Defect defect = this.getById(id);
        if (defect != null) {
            defect.setAssignee(assignee);
            this.updateById(defect);
            fillDefectInfo(defect);
        }
        return defect;
    }

    @Override
    public Map<String, Object> getStatistics(Long projectId) {
        Map<String, Object> stats = new HashMap<>();
        
        if (projectId != null) {
            // 统计所有属于该项目的缺陷：直接关联 + 通过需求关联
            LambdaQueryWrapper<Defect> wrapper = new LambdaQueryWrapper<>();
            wrapper.and(w -> w.eq(Defect::getProjectId, projectId)
                    .or()
                    .exists("SELECT 1 FROM requirement r WHERE r.id = defect.requirement_id AND r.project_id = {0}", projectId));
            stats.put("total", this.count(wrapper));
            
            LambdaQueryWrapper<Defect> openWrapper = new LambdaQueryWrapper<>();
            openWrapper.and(w -> w.eq(Defect::getProjectId, projectId)
                    .or()
                    .exists("SELECT 1 FROM requirement r WHERE r.id = defect.requirement_id AND r.project_id = {0}", projectId));
            openWrapper.ne(Defect::getStatus, "RESOLVED").ne(Defect::getStatus, "CLOSED");
            stats.put("open", this.count(openWrapper));
        } else {
            stats.put("total", this.count());
            LambdaQueryWrapper<Defect> openWrapper = new LambdaQueryWrapper<>();
            openWrapper.ne(Defect::getStatus, "RESOLVED").ne(Defect::getStatus, "CLOSED");
            stats.put("open", this.count(openWrapper));
        }
        
        return stats;
    }
}
