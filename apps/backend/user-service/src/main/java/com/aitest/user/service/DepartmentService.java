package com.aitest.user.service;

import com.aitest.common.exception.BusinessException;
import com.aitest.user.dto.DepartmentCreateDTO;
import com.aitest.user.dto.DepartmentUpdateDTO;
import com.aitest.user.dto.DepartmentVO;
import com.aitest.user.entity.Department;
import com.aitest.user.mapper.DepartmentMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 部门服务
 */
@Service
public class DepartmentService extends ServiceImpl<DepartmentMapper, Department> {

    private final DepartmentMapper departmentMapper;

    public DepartmentService(DepartmentMapper departmentMapper) {
        this.departmentMapper = departmentMapper;
    }

    /**
     * 获取部门树
     */
    public List<DepartmentVO> getDepartmentTree() {
        List<Department> departments = list(new LambdaQueryWrapper<Department>()
                .eq(Department::getDeleted, 0)
                .orderByAsc(Department::getSortOrder));

        Map<Long, List<Department>> parentMap = departments.stream()
                .collect(Collectors.groupingBy(Department::getParentId));

        List<DepartmentVO> result = new ArrayList<>();
        buildTree(0L, parentMap, departments, result);
        return result;
    }

    /**
     * 递归构建部门树
     */
    private void buildTree(Long parentId, Map<Long, List<Department>> parentMap,
                           List<Department> allDepartments, List<DepartmentVO> result) {
        List<Department> children = parentMap.getOrDefault(parentId, new ArrayList<>());
        for (Department dept : children) {
            DepartmentVO vo = convertToVO(dept);
            List<DepartmentVO> childVOs = new ArrayList<>();
            buildTree(dept.getId(), parentMap, allDepartments, childVOs);
            vo.setChildren(childVOs);
            result.add(vo);
        }
    }

    /**
     * 创建部门
     */
    @Transactional(rollbackFor = Exception.class)
    public Department createDepartment(DepartmentCreateDTO dto) {
        Department exist = getOne(new LambdaQueryWrapper<Department>()
                .eq(Department::getCode, dto.getCode())
                .eq(Department::getDeleted, 0));
        if (exist != null) {
            throw new BusinessException("部门编码已存在");
        }

        Department department = new Department();
        BeanUtils.copyProperties(dto, department);
        department.setStatus(1);
        department.setDeleted(0);
        save(department);
        return department;
    }

    /**
     * 更新部门
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateDepartment(Long departmentId, DepartmentUpdateDTO dto) {
        Department exist = getById(departmentId);
        if (exist == null || exist.getDeleted() == 1) {
            throw new BusinessException("部门不存在");
        }

        if (dto.getCode() != null && !dto.getCode().equals(exist.getCode())) {
            Department codeExist = getOne(new LambdaQueryWrapper<Department>()
                    .eq(Department::getCode, dto.getCode())
                    .eq(Department::getDeleted, 0));
            if (codeExist != null) {
                throw new BusinessException("部门编码已存在");
            }
        }

        BeanUtils.copyProperties(dto, exist);
        updateById(exist);
    }

    /**
     * 删除部门（软删除）
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteDepartment(Long departmentId) {
        Department department = getById(departmentId);
        if (department == null || department.getDeleted() == 1) {
            throw new BusinessException("部门不存在");
        }

        // 检查是否有子部门
        long childCount = count(new LambdaQueryWrapper<Department>()
                .eq(Department::getParentId, departmentId)
                .eq(Department::getDeleted, 0));
        if (childCount > 0) {
            throw new BusinessException("请先删除子部门");
        }

        department.setDeleted(1);
        updateById(department);
    }

    /**
     * 获取部门详情
     */
    public DepartmentVO getDepartmentById(Long departmentId) {
        Department department = getById(departmentId);
        if (department == null || department.getDeleted() == 1) {
            throw new BusinessException("部门不存在");
        }
        return convertToVO(department);
    }

    /**
     * 获取所有子部门ID列表（递归）
     */
    public List<Long> getChildDepartmentIds(Long parentId) {
        List<Department> children = list(new LambdaQueryWrapper<Department>()
                .eq(Department::getParentId, parentId)
                .eq(Department::getDeleted, 0));
        List<Long> ids = new ArrayList<>();
        for (Department child : children) {
            ids.add(child.getId());
            ids.addAll(getChildDepartmentIds(child.getId()));
        }
        return ids;
    }

    /**
     * 获取所有部门列表（非树形）
     */
    public List<DepartmentVO> getAllDepartments() {
        List<Department> departments = list(new LambdaQueryWrapper<Department>()
                .eq(Department::getDeleted, 0)
                .orderByAsc(Department::getSortOrder));
        return departments.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    /**
     * 转换为VO
     */
    private DepartmentVO convertToVO(Department department) {
        DepartmentVO vo = new DepartmentVO();
        BeanUtils.copyProperties(department, vo);
        return vo;
    }
}
