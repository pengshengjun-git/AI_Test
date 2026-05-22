package com.aitest.user.controller;

import com.aitest.common.result.Result;
import com.aitest.user.dto.DepartmentCreateDTO;
import com.aitest.user.dto.DepartmentUpdateDTO;
import com.aitest.user.dto.DepartmentVO;
import com.aitest.user.service.DepartmentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门管理控制器
 */
@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    /**
     * 获取部门树
     */
    @GetMapping("/tree")
    public Result<List<DepartmentVO>> getDepartmentTree() {
        List<DepartmentVO> departments = departmentService.getDepartmentTree();
        return Result.success(departments);
    }

    /**
     * 获取所有部门列表
     */
    @GetMapping
    public Result<List<DepartmentVO>> getAllDepartments() {
        List<DepartmentVO> departments = departmentService.getAllDepartments();
        return Result.success(departments);
    }

    /**
     * 获取部门详情
     */
    @GetMapping("/{id}")
    public Result<DepartmentVO> getDepartment(@PathVariable Long id) {
        DepartmentVO department = departmentService.getDepartmentById(id);
        return Result.success(department);
    }

    /**
     * 创建部门
     */
    @PostMapping
    public Result<DepartmentVO> createDepartment(@RequestBody DepartmentCreateDTO dto) {
        departmentService.createDepartment(dto);
        return Result.success();
    }

    /**
     * 更新部门
     */
    @PutMapping("/{id}")
    public Result<Void> updateDepartment(@PathVariable Long id, @RequestBody DepartmentUpdateDTO dto) {
        departmentService.updateDepartment(id, dto);
        return Result.success();
    }

    /**
     * 删除部门
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return Result.success();
    }
}
