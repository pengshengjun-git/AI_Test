# Tasks - 权限控制系统实现

## Task 1: 后端 - 用户角色信息扩展
- [x] Task 1.1: 更新登录接口，返回用户角色和权限码列表
- [x] Task 1.2: 创建权限注解 `@RequirePermission`
- [x] Task 1.3: 实现权限校验拦截器
- [x] Task 1.4: 创建权限工具类 `PermissionContext`

## Task 2: 后端 - 数据范围过滤
- [x] Task 2.1: 在 UserService 中实现基于部门的数据过滤
- [x] Task 2.2: 在 DepartmentService 中实现基于部门的数据过滤
- [ ] Task 2.3: 在 ProjectService 中实现基于部门的数据过滤

## Task 3: 后端 - 管理接口权限保护
- [x] Task 3.1: 在用户管理 Controller 添加 `@RequirePermission("system:user:*")`
- [x] Task 3.2: 在角色管理 Controller 添加 `@RequirePermission("system:role:*")`
- [x] Task 3.3: 在部门管理 Controller 添加 `@RequirePermission("system:department:*")`
- [x] Task 3.4: 在菜单管理 Controller 添加 `@RequirePermission("system:menu:*")`

## Task 4: 前端 - 用户状态管理
- [x] Task 4.1: 更新用户信息存储结构，包含 roles 和 permissions 字段
- [x] Task 4.2: 创建权限判断工具函数 `hasPermission()`
- [x] Task 4.3: 创建角色判断工具函数 `isAdmin()`

## Task 5: 前端 - 菜单权限控制
- [x] Task 5.1: 修改侧边栏菜单，根据用户角色动态显示/隐藏
- [x] Task 5.2: 创建 v-permission 指令用于按钮级权限控制
- [x] Task 5.3: 在用户管理页面应用权限指令

## Task 6: 前端 - API权限校验
- [x] Task 6.1: 创建 API 权限拦截器
- [x] Task 6.2: 处理 403 响应，显示无权限提示

## Task 7: 测试与验证
- [x] Task 7.1: 测试管理员登录，验证所有管理功能可访问
- [ ] Task 7.2: 测试普通用户登录，验证菜单隐藏
- [ ] Task 7.3: 测试普通用户尝试访问管理接口，返回403
- [ ] Task 7.4: 验证数据范围过滤正确生效

## Task Dependencies
- Task 1, 2, 3 之间无依赖，可并行开发
- Task 4 依赖 Task 1（需要后端返回权限信息）
- Task 5, 6 依赖 Task 4
- Task 7 依赖 Task 1-6 全部完成

## Implementation Order
1. Task 1 (后端权限基础) - Day 1 ✅
2. Task 2, 3 (后端权限保护和数据过滤) - Day 1 ✅
3. Task 4 (前端用户状态) - Day 2 ✅
4. Task 5, 6 (前端权限控制) - Day 2 ✅
5. Task 7 (测试验证) - Day 2 ⏳

## 待验证项
- 普通用户登录验证
- 数据范围过滤验证
- API 403 响应验证
