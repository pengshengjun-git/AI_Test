package com.aitest.user.service;

import com.aitest.common.exception.BusinessException;
import com.aitest.user.dto.MenuCreateDTO;
import com.aitest.user.dto.MenuUpdateDTO;
import com.aitest.user.dto.MenuVO;
import com.aitest.user.entity.Menu;
import com.aitest.user.mapper.MenuMapper;
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
 * 菜单服务
 */
@Service
public class MenuService extends ServiceImpl<MenuMapper, Menu> {

    private final MenuMapper menuMapper;

    public MenuService(MenuMapper menuMapper) {
        this.menuMapper = menuMapper;
    }

    /**
     * 获取菜单树
     */
    public List<MenuVO> getMenuTree() {
        List<Menu> menus = list(new LambdaQueryWrapper<Menu>()
                .eq(Menu::getDeleted, 0)
                .orderByAsc(Menu::getSortOrder));

        Map<Long, List<Menu>> parentMap = menus.stream()
                .collect(Collectors.groupingBy(Menu::getParentId));

        List<MenuVO> result = new ArrayList<>();
        buildTree(0L, parentMap, result);
        return result;
    }

    /**
     * 递归构建菜单树
     */
    private void buildTree(Long parentId, Map<Long, List<Menu>> parentMap, List<MenuVO> result) {
        List<Menu> children = parentMap.getOrDefault(parentId, new ArrayList<>());
        for (Menu menu : children) {
            MenuVO vo = convertToVO(menu);
            List<MenuVO> childVOs = new ArrayList<>();
            buildTree(menu.getId(), parentMap, childVOs);
            vo.setChildren(childVOs);
            result.add(vo);
        }
    }

    /**
     * 创建菜单
     */
    @Transactional(rollbackFor = Exception.class)
    public Menu createMenu(MenuCreateDTO dto) {
        Menu menu = new Menu();
        BeanUtils.copyProperties(dto, menu);
        menu.setDeleted(0);
        save(menu);
        return menu;
    }

    /**
     * 更新菜单
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateMenu(Long menuId, MenuUpdateDTO dto) {
        Menu exist = getById(menuId);
        if (exist == null || exist.getDeleted() == 1) {
            throw new BusinessException("菜单不存在");
        }

        BeanUtils.copyProperties(dto, exist);
        updateById(exist);
    }

    /**
     * 删除菜单（软删除）
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteMenu(Long menuId) {
        Menu menu = getById(menuId);
        if (menu == null || menu.getDeleted() == 1) {
            throw new BusinessException("菜单不存在");
        }

        // 检查是否有子菜单
        long childCount = count(new LambdaQueryWrapper<Menu>()
                .eq(Menu::getParentId, menuId)
                .eq(Menu::getDeleted, 0));
        if (childCount > 0) {
            throw new BusinessException("请先删除子菜单");
        }

        menu.setDeleted(1);
        updateById(menu);
    }

    /**
     * 获取菜单详情
     */
    public MenuVO getMenuById(Long menuId) {
        Menu menu = getById(menuId);
        if (menu == null || menu.getDeleted() == 1) {
            throw new BusinessException("菜单不存在");
        }
        return convertToVO(menu);
    }

    /**
     * 获取所有菜单列表（非树形）
     */
    public List<MenuVO> getAllMenus() {
        List<Menu> menus = list(new LambdaQueryWrapper<Menu>()
                .eq(Menu::getDeleted, 0)
                .orderByAsc(Menu::getSortOrder));
        return menus.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    /**
     * 获取所有按钮权限列表
     */
    public List<MenuVO> getButtonPermissions() {
        List<Menu> menus = list(new LambdaQueryWrapper<Menu>()
                .eq(Menu::getDeleted, 0)
                .eq(Menu::getType, "button")
                .orderByAsc(Menu::getSortOrder));
        return menus.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    /**
     * 转换为VO
     */
    private MenuVO convertToVO(Menu menu) {
        MenuVO vo = new MenuVO();
        BeanUtils.copyProperties(menu, vo);
        return vo;
    }
}
