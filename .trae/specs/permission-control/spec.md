# 权限控制规范 - Spec

## Overview
- **Summary**: 实现基于角色的权限控制系统，管理员可管理所有用户/部门/菜单，非管理员只能访问权限范围内的数据
- **Purpose**: 确保系统数据安全，实现精细化的权限管控
- **Target Users**: 所有系统用户（管理员vs普通用户）

## Why
当前系统缺少基于角色的权限控制，普通用户可以看到并操作不应该有权限的数据。需要实现：
1. 前端菜单根据角色动态显示/隐藏
2. 后端API根据用户角色过滤数据
3. 敏感操作仅管理员可执行

## What Changes

### 后端改动
- 新增权限注解 `@RequirePermission`
- 实现数据范围过滤（基于用户部门）
- 管理员接口白名单
- 登录响应增加角色和权限信息

### 前端改动
- 侧边栏菜单根据用户角色动态显示
- 新增权限指令 `v-permission`
- 敏感按钮（如删除/禁用）根据权限显示

## Impact
- **Affected specs**: FR-1 用户权限管理
- **Affected code**: 
  - 后端: UserService, RoleService, DepartmentService, MenuService
  - 前端: App.vue, 各管理页面组件

## ADDED Requirements

### Requirement: 管理员权限判断
系统 SHALL 识别具有 ADMIN 角色的用户

#### Scenario: 管理员访问受保护接口
- **WHEN** 用户角色包含 "ADMIN" 或权限码包含 "system:*"
- **THEN** 允许访问所有管理接口

### Requirement: 普通用户数据范围限制
普通用户 SHALL 只看到自己部门及下级部门的数据

#### Scenario: 普通用户查询用户列表
- **WHEN** 用户角色为 "DEVELOPER" 或 "TESTER"
- **THEN** 仅返回该用户所在部门及其子部门下的用户数据

### Requirement: 前端菜单权限控制
前端 SHALL 根据用户角色动态显示/隐藏菜单项

#### Scenario: 普通用户登录
- **WHEN** 用户角色不包含 ADMIN
- **THEN** 侧边栏不显示"系统管理"分组及其子菜单

### Requirement: 按钮级权限控制
前端 SHALL 根据用户权限控制按钮的显示/隐藏

#### Scenario: 非管理员查看用户列表
- **WHEN** 用户不具有用户管理权限
- **THEN** 不显示"编辑"、"删除"、"禁用"按钮

## MODIFIED Requirements

### Requirement: 用户管理API权限
原有用户管理API SHALL 增加权限校验

- `GET /api/users` - 需 ADMIN 角色或 `user:read` 权限
- `POST /api/users` - 需 ADMIN 角色或 `user:create` 权限
- `PUT /api/users/{id}` - 需 ADMIN 角色或 `user:update` 权限
- `DELETE /api/users/{id}` - 需 ADMIN 角色或 `user:delete` 权限
- `PUT /api/users/{id}/disable` - 需 ADMIN 角色
- `PUT /api/users/{id}/enable` - 需 ADMIN 角色

### Requirement: 角色管理API权限
原有角色管理API SHALL 增加权限校验

- 所有角色管理接口 - 需 ADMIN 角色

### Requirement: 部门管理API权限
原有部门管理API SHALL 增加权限校验

- 所有部门管理接口 - 需 ADMIN 角色

### Requirement: 菜单管理API权限
原有菜单管理API SHALL 增加权限校验

- 所有菜单管理接口 - 需 ADMIN 角色

## Data Model

### 用户角色映射
```
用户 -> 角色 -> 菜单权限 -> 数据范围
```

### 角色定义
| 角色代码 | 角色名称 | 数据范围 | 可管理模块 |
|---------|---------|---------|-----------|
| ADMIN | 超级管理员 | 全部 | 全部 |
| DEVELOPER | 开发人员 | 本部门及子部门 | 项目、需求、用例、缺陷 |
| TESTER | 测试人员 | 本部门及子部门 | 用例、缺陷、覆盖率 |
| PRODUCT | 产品经理 | 本部门及子部门 | 需求、项目、覆盖率 |

### 权限码定义
| 权限码 | 权限名称 | 说明 |
|-------|---------|-----|
| system:* | 系统管理 | 所有系统管理权限 |
| system:user:* | 用户管理 | 用户管理全部权限 |
| system:user:read | 查看用户 | 查看用户列表和详情 |
| system:user:create | 创建用户 | 创建新用户 |
| system:user:update | 更新用户 | 更新用户信息 |
| system:user:delete | 删除用户 | 删除用户 |
| system:role:* | 角色管理 | 角色管理全部权限 |
| system:department:* | 部门管理 | 部门管理全部权限 |
| system:menu:* | 菜单管理 | 菜单管理全部权限 |

## Acceptance Criteria

### AC-1: 管理员权限验证
- **Given**: 用户以 ADMIN 角色登录
- **When**: 访问 /system/users, /system/roles, /system/departments, /system/menus
- **Then**: 允许访问，所有功能正常

### AC-2: 普通用户菜单隐藏
- **Given**: 用户以 DEVELOPER/TESTER/PRODUCT 角色登录
- **When**: 查看侧边栏菜单
- **Then**: 不显示"系统管理"分组及其子菜单

### AC-3: 普通用户数据范围
- **Given**: 用户属于"后端组"（有子部门"Java组"）
- **When**: 查询用户列表
- **Then**: 仅返回"后端组"和"Java组"下的用户

### AC-4: 权限按钮隐藏
- **Given**: 普通用户查看用户管理页面
- **When**: 查看操作列
- **Then**: 不显示"编辑"、"删除"、"禁用"按钮

### AC-5: API权限拦截
- **Given**: 普通用户尝试调用管理接口
- **When**: 发送 POST /api/users
- **Then**: 返回 403 Forbidden
