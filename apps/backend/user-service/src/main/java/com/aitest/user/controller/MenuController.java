package com.aitest.user.controller;

import com.aitest.common.result.Result;
import com.aitest.user.dto.MenuCreateDTO;
import com.aitest.user.dto.MenuUpdateDTO;
import com.aitest.user.dto.MenuVO;
import com.aitest.user.service.MenuService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单管理控制器
 */
@RestController
@RequestMapping("/api/menus")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    /**
     * 获取菜单树
     */
    @GetMapping("/tree")
    public Result<List<MenuVO>> getMenuTree() {
        List<MenuVO> menus = menuService.getMenuTree();
        return Result.success(menus);
    }

    /**
     * 获取所有菜单列表
     */
    @GetMapping
    public Result<List<MenuVO>> getAllMenus() {
        List<MenuVO> menus = menuService.getAllMenus();
        return Result.success(menus);
    }

    /**
     * 获取菜单详情
     */
    @GetMapping("/{id}")
    public Result<MenuVO> getMenu(@PathVariable Long id) {
        MenuVO menu = menuService.getMenuById(id);
        return Result.success(menu);
    }

    /**
     * 创建菜单
     */
    @PostMapping
    public Result<MenuVO> createMenu(@RequestBody MenuCreateDTO dto) {
        menuService.createMenu(dto);
        return Result.success();
    }

    /**
     * 更新菜单
     */
    @PutMapping("/{id}")
    public Result<Void> updateMenu(@PathVariable Long id, @RequestBody MenuUpdateDTO dto) {
        menuService.updateMenu(id, dto);
        return Result.success();
    }

    /**
     * 删除菜单
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteMenu(@PathVariable Long id) {
        menuService.deleteMenu(id);
        return Result.success();
    }

    /**
     * 获取按钮权限列表
     */
    @GetMapping("/permissions")
    public Result<List<MenuVO>> getButtonPermissions() {
        List<MenuVO> permissions = menuService.getButtonPermissions();
        return Result.success(permissions);
    }
}
