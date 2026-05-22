package com.aitest.user.controller;

import com.aitest.common.result.Result;
import com.aitest.user.dto.RoleCreateDTO;
import com.aitest.user.dto.RoleQueryDTO;
import com.aitest.user.dto.RoleUpdateDTO;
import com.aitest.user.dto.RoleVO;
import com.aitest.user.service.RoleService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理控制器
 */
@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * 分页查询角色列表
     */
    @GetMapping
    public Result<IPage<RoleVO>> listRoles(RoleQueryDTO query) {
        IPage<RoleVO> page = roleService.queryRoles(query);
        return Result.success(page);
    }

    /**
     * 获取所有角色列表
     */
    @GetMapping("/all")
    public Result<List<RoleVO>> getAllRoles() {
        List<RoleVO> roles = roleService.getAllRoles();
        return Result.success(roles);
    }

    /**
     * 获取角色详情
     */
    @GetMapping("/{id}")
    public Result<RoleVO> getRole(@PathVariable Long id) {
        RoleVO role = roleService.getRoleById(id);
        return Result.success(role);
    }

    /**
     * 创建角色
     */
    @PostMapping
    public Result<RoleVO> createRole(@RequestBody RoleCreateDTO dto) {
        RoleVO role = roleService.createRole(dto);
        return Result.success(role);
    }

    /**
     * 更新角色
     */
    @PutMapping("/{id}")
    public Result<Void> updateRole(@PathVariable Long id, @RequestBody RoleUpdateDTO dto) {
        roleService.updateRole(id, dto);
        return Result.success();
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return Result.success();
    }
}
