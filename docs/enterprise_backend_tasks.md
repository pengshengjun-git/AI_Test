# 企业级后台管理系统 - 开发任务清单

## 第一阶段：数据库层

### 1.1 数据库表创建
- [ ] 执行 `schema_extension.sql` 创建新表
- [ ] 验证表结构是否正确
- [ ] 插入初始化数据（菜单、部门、角色）

---

## 第二阶段：后端开发 - user-service

### 2.1 实体类完善
- [x] 创建 `Department` 实体类
- [x] 创建 `Menu` 实体类
- [x] 创建 `RoleMenu` 实体类
- [x] 创建 `AiCallRecord` 实体类
- [ ] 完善 `User` 实体类（如有需要）
- [ ] 完善 `Role` 实体类（如有需要）

### 2.2 Mapper层
- [x] 创建 `DepartmentMapper` 接口
- [x] 创建 `MenuMapper` 接口
- [x] 创建 `RoleMenuMapper` 接口
- [x] 创建 `AiCallRecordMapper` 接口
- [x] 创建 `RoleMenuMapper.xml` 映射文件

### 2.3 Service层
- [ ] 创建 `DepartmentService` 服务类
  - [ ] 获取部门树
  - [ ] 创建部门
  - [ ] 更新部门
  - [ ] 删除部门
- [ ] 创建 `MenuService` 服务类
  - [ ] 获取菜单树
  - [ ] 创建菜单
  - [ ] 更新菜单
  - [ ] 删除菜单
  - [ ] 根据用户ID获取菜单
- [ ] 创建 `AiCallRecordService` 服务类
  - [ ] 分页查询调用记录
  - [ ] 获取调用记录详情
  - [ ] 统计分析
- [ ] 完善 `UserService` 服务类
  - [ ] 用户列表查询（分页、筛选）
  - [ ] 创建用户
  - [ ] 更新用户
  - [ ] 删除用户
  - [ ] 禁用/启用用户
  - [ ] 重置密码
- [ ] 完善 `RoleService` 服务类
  - [ ] 角色列表
  - [ ] 创建角色
  - [ ] 更新角色
  - [ ] 删除角色
  - [ ] 分配角色菜单
  - [ ] 获取角色菜单

### 2.4 DTO层
- [ ] 创建 `UserQueryDTO`（用户查询条件）
- [ ] 创建 `UserCreateDTO`（用户创建请求）
- [ ] 创建 `UserUpdateDTO`（用户更新请求）
- [ ] 创建 `RoleQueryDTO`（角色查询条件）
- [ ] 创建 `RoleCreateDTO`（角色创建请求）
- [ ] 创建 `RoleUpdateDTO`（角色更新请求）
- [ ] 创建 `DepartmentQueryDTO`（部门查询条件）
- [ ] 创建 `DepartmentCreateDTO`（部门创建请求）
- [ ] 创建 `DepartmentUpdateDTO`（部门更新请求）
- [ ] 创建 `MenuQueryDTO`（菜单查询条件）
- [ ] 创建 `MenuCreateDTO`（菜单创建请求）
- [ ] 创建 `MenuUpdateDTO`（菜单更新请求）
- [ ] 创建 `AiCallRecordQueryDTO`（AI调用记录查询条件）
- [ ] 创建 `AiCallRecordStatisticsDTO`（AI调用统计响应）

### 2.5 Controller层
- [ ] 创建 `DepartmentController` 控制器
  - [ ] GET /api/v1/departments - 获取部门树
  - [ ] GET /api/v1/departments/{id} - 获取部门详情
  - [ ] POST /api/v1/departments - 创建部门
  - [ ] PUT /api/v1/departments/{id} - 更新部门
  - [ ] DELETE /api/v1/departments/{id} - 删除部门
- [ ] 创建 `MenuController` 控制器
  - [ ] GET /api/v1/menus - 获取菜单树
  - [ ] GET /api/v1/menus/{id} - 获取菜单详情
  - [ ] POST /api/v1/menus - 创建菜单
  - [ ] PUT /api/v1/menus/{id} - 更新菜单
  - [ ] DELETE /api/v1/menus/{id} - 删除菜单
  - [ ] GET /api/v1/menus/user - 获取当前用户菜单
- [ ] 创建 `AiCallRecordController` 控制器
  - [ ] GET /api/v1/ai/call-records - 调用记录列表
  - [ ] GET /api/v1/ai/call-records/{id} - 调用记录详情
  - [ ] GET /api/v1/ai/statistics - 统计分析
- [ ] 完善 `UserController` 控制器
  - [ ] GET /api/v1/users - 用户列表（分页、筛选）
  - [ ] PUT /api/v1/users/{id}/status - 禁用/启用用户
  - [ ] PUT /api/v1/users/{id}/reset-password - 重置密码
- [ ] 完善 `RoleController` 控制器
  - [ ] GET /api/v1/roles/{id}/menus - 获取角色菜单
  - [ ] PUT /api/v1/roles/{id}/menus - 分配角色菜单

### 2.6 配置层
- [ ] 完善 `SecurityConfig` - 权限拦截配置
- [ ] 完善 `MybatisPlusConfig` - MyBatis Plus配置

---

## 第三阶段：后端开发 - dashboard-service

### 3.1 DTO层
- [ ] 创建 `DashboardOverviewDTO`（概览统计响应）
- [ ] 创建 `ProjectStatisticsDTO`（项目统计响应）
- [ ] 创建 `TestStatisticsDTO`（测试统计响应）
- [ ] 创建 `TrendDataDTO`（趋势数据响应）

### 3.2 Service层
- [ ] 创建 `DashboardService` 服务类
  - [ ] 获取概览统计
  - [ ] 获取项目统计
  - [ ] 获取测试统计
  - [ ] 获取趋势数据

### 3.3 Controller层
- [ ] 创建 `DashboardController` 控制器
  - [ ] GET /api/v1/dashboard/overview - 概览统计
  - [ ] GET /api/v1/dashboard/project-stats - 项目统计
  - [ ] GET /api/v1/dashboard/test-stats - 测试统计
  - [ ] GET /api/v1/dashboard/trend - 趋势数据

---

## 第四阶段：前端开发

### 4.1 前端基础
- [ ] 创建前端路由配置
- [ ] 创建前端API服务模块
- [ ] 创建前端状态管理

### 4.2 用户管理模块
- [ ] 用户列表页面
- [ ] 用户创建/编辑弹窗
- [ ] 用户删除确认
- [ ] 用户禁用/启用
- [ ] 用户重置密码

### 4.3 角色管理模块
- [ ] 角色列表页面
- [ ] 角色创建/编辑弹窗
- [ ] 角色删除确认
- [ ] 角色菜单分配

### 4.4 部门管理模块
- [ ] 部门树展示
- [ ] 部门创建/编辑弹窗
- [ ] 部门删除确认

### 4.5 菜单管理模块
- [ ] 菜单树展示
- [ ] 菜单创建/编辑弹窗
- [ ] 菜单删除确认

### 4.6 数据看板模块
- [ ] 概览页面
- [ ] 项目统计页面
- [ ] 测试统计页面

### 4.7 AI工具箱模块
- [ ] 调用记录页面
- [ ] 统计分析页面

---

## 第五阶段：测试与优化

### 5.1 单元测试
- [ ] Service层单元测试
- [ ] Mapper层单元测试

### 5.2 集成测试
- [ ] API接口集成测试
- [ ] 权限验证测试

### 5.3 性能优化
- [ ] 慢查询优化
- [ ] 缓存策略实现
- [ ] 接口响应时间优化

### 5.4 安全加固
- [ ] SQL注入测试
- [ ] XSS防护测试
- [ ] 权限绕过测试
- [ ] 敏感数据脱敏

---

## 第六阶段：部署上线

### 6.1 部署准备
- [ ] 数据库迁移脚本
- [ ] 配置文件准备
- [ ] Docker镜像构建

### 6.2 上线部署
- [ ] 测试环境部署
- [ ] 生产环境部署
- [ ] 数据迁移

---

## 开发优先级

### P0 - 必须完成（MVP）
- 数据库表创建
- 用户管理功能
- 角色管理功能
- 菜单管理功能
- 基础数据看板
- AI调用记录管理

### P1 - 重要功能
- 部门管理功能
- 数据看板高级功能
- AI调用统计分析

### P2 - 优化增强
- 性能优化
- 安全加固
- 操作日志审计
- 数据级权限控制
