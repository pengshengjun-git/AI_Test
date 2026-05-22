# AI测试管理平台 - 企业级后台管理系统架构设计

## 一、概述

本文档描述了AI测试管理平台企业级后台管理系统的架构设计，包括用户管理、角色权限、数据看板、AI工具箱四大核心模块。

## 二、系统架构

### 2.1 整体架构图

```
┌─────────────────────────────────────────────────────────────┐
│                        前端应用层                             │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐     │
│  │ 用户管理 │  │ 角色权限 │  │ 数据看板 │  │AI工具箱 │     │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘     │
└─────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────┐
│                        微服务层                               │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐     │
│  │ auth-service │  │ user-service │  │dashboard-svc│     │
│  └──────────────┘  └──────────────┘  └──────────────┘     │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐     │
│  │project-svc   │  │ defect-svc   │  │ ai-service  │     │
│  └──────────────┘  └──────────────┘  └──────────────┘     │
└─────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────┐
│                        数据存储层                             │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐                 │
│  │  MySQL   │  │  Redis   │  │  MinIO   │                 │
│  └──────────┘  └──────────┘  └──────────┘                 │
└─────────────────────────────────────────────────────────────┘
```

### 2.2 服务职责划分

| 服务 | 端口 | 主要职责 |
|------|------|---------|
| auth-service | 8001 | 用户认证、JWT Token生成与验证 |
| user-service | 8002 | 用户管理、角色管理、权限管理、部门管理、菜单管理、AI调用记录管理 |
| dashboard-service | 8003 | 数据看板、统计分析 |
| project-service | 8004 | 项目管理、项目成员管理 |
| requirement-service | 8005 | 需求管理 |
| testcase-service | 8006 | 测试用例管理 |
| defect-service | 8007 | 缺陷管理 |
| strategy-service | 8008 | 策略管理 |
| ai-service | 8009 | AI能力调用（可扩展） |

## 三、数据库设计

### 3.1 核心数据表

#### 用户与权限相关表（已存在）
- `user`: 用户表
- `role`: 角色表
- `permission`: 权限表
- `user_role`: 用户角色关联表
- `role_permission`: 角色权限关联表

#### 新增表
- `department`: 部门表
- `menu`: 菜单表
- `role_menu`: 角色菜单关联表
- `ai_call_record`: AI调用记录表

### 3.2 ER关系图

```
user (用户表)
  ├── user_role (用户角色关联)
  │     └── role (角色表)
  │           ├── role_permission (角色权限关联)
  │           │     └── permission (权限表)
  │           └── role_menu (角色菜单关联)
  │                 └── menu (菜单表)
  ├── department (部门表 - 通过 department_id 关联)
  └── ai_call_record (AI调用记录表 - 通过 user_id 关联)

project (项目表)
  ├── project_member (项目成员表)
  ├── requirement (需求表)
  │     └── requirement_test_point (需求测试点表)
  ├── testcase (测试用例表)
  │     └── testcase_step (测试用例步骤表)
  └── defect (缺陷表)
        └── defect_comment (缺陷评论表)
```

### 3.3 部门表结构

| 字段名 | 类型 | 说明 | 约束 |
|--------|------|------|------|
| id | BIGINT | 部门ID | 主键，自增 |
| parent_id | BIGINT | 父部门ID | 0表示根部门 |
| name | VARCHAR(128) | 部门名称 | 非空 |
| code | VARCHAR(64) | 部门编码 | 唯一，非空 |
| leader_id | BIGINT | 负责人ID | 外键关联user表 |
| sort_order | INT | 排序 | 默认0 |
| description | VARCHAR(512) | 部门描述 | |
| status | TINYINT | 状态 | 0-禁用, 1-启用 |
| created_at | DATETIME | 创建时间 | 自动生成 |
| updated_at | DATETIME | 更新时间 | 自动更新 |
| deleted | TINYINT | 删除标记 | 0-未删除, 1-已删除 |

### 3.4 菜单表结构

| 字段名 | 类型 | 说明 | 约束 |
|--------|------|------|------|
| id | BIGINT | 菜单ID | 主键，自增 |
| parent_id | BIGINT | 父菜单ID | 0表示根菜单 |
| name | VARCHAR(128) | 菜单名称 | 非空 |
| code | VARCHAR(128) | 菜单编码 | 唯一，非空 |
| path | VARCHAR(256) | 路由路径 | |
| component | VARCHAR(256) | 组件路径 | |
| icon | VARCHAR(128) | 图标 | |
| type | VARCHAR(32) | 类型 | directory-目录, menu-菜单, button-按钮 |
| permission | VARCHAR(256) | 权限标识 | |
| sort_order | INT | 排序 | 默认0 |
| visible | TINYINT | 是否可见 | 0-隐藏, 1-显示 |
| cacheable | TINYINT | 是否缓存 | 0-否, 1-是 |
| redirect | VARCHAR(256) | 重定向路径 | |
| description | VARCHAR(512) | 描述 | |
| created_at | DATETIME | 创建时间 | 自动生成 |
| updated_at | DATETIME | 更新时间 | 自动更新 |
| deleted | TINYINT | 删除标记 | 0-未删除, 1-已删除 |

### 3.5 AI调用记录表结构

| 字段名 | 类型 | 说明 | 约束 |
|--------|------|------|------|
| id | BIGINT | 记录ID | 主键，自增 |
| user_id | BIGINT | 用户ID | 非空，外键关联user表 |
| project_id | BIGINT | 项目ID | 外键关联project表 |
| function_type | VARCHAR(64) | 功能类型 | requirement_analysis, case_generate, defect_analysis, report_generate, risk_analysis |
| model_name | VARCHAR(128) | 模型名称 | 非空 |
| prompt_tokens | INT | Prompt Token数 | 默认0 |
| completion_tokens | INT | Completion Token数 | 默认0 |
| total_tokens | INT | 总Token数 | 默认0 |
| status | VARCHAR(32) | 状态 | success-成功, failed-失败 |
| error_message | TEXT | 错误信息 | |
| response_time | BIGINT | 响应时间(毫秒) | |
| request_id | VARCHAR(128) | 请求ID | |
| input_data | TEXT | 输入数据JSON | |
| output_data | TEXT | 输出数据JSON | |
| cost | DECIMAL(10,6) | 成本 | |
| ip_address | VARCHAR(64) | IP地址 | |
| created_at | DATETIME | 创建时间 | 自动生成 |

## 四、权限设计

### 4.1 RBAC权限模型

系统采用RBAC（基于角色的访问控制）权限模型：

```
User (用户)  1:n  UserRole (用户角色关联)  n:1  Role (角色)
Role (角色)  1:n  RolePermission (角色权限关联)  n:1  Permission (权限)
Role (角色)  1:n  RoleMenu (角色菜单关联)  n:1  Menu (菜单)
```

### 4.2 权限粒度

1. **菜单级权限**: 控制用户可见的菜单和页面
2. **按钮级权限**: 控制页面上按钮的显示和禁用
3. **数据级权限**: 控制用户可访问的数据范围（如组织数据隔离）
4. **接口级权限**: 控制后端接口的访问权限

### 4.3 权限验证流程

```
用户请求 → JWT Token解析 → 获取用户角色 → 获取角色权限/菜单 → 
权限验证 → 业务处理 → 返回结果
```

## 五、接口设计规范

### 5.1 统一响应格式

```json
{
  "code": 200,
  "message": "success",
  "data": {},
  "timestamp": 1680000000000
}
```

### 5.2 状态码规范

| 状态码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未授权 |
| 403 | 禁止访问（权限不足） |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

### 5.3 核心接口列表

#### 用户管理接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/v1/users | 用户列表（分页） |
| GET | /api/v1/users/{id} | 用户详情 |
| POST | /api/v1/users | 创建用户 |
| PUT | /api/v1/users/{id} | 更新用户 |
| DELETE | /api/v1/users/{id} | 删除用户 |
| PUT | /api/v1/users/{id}/status | 禁用/启用用户 |
| PUT | /api/v1/users/{id}/reset-password | 重置密码 |

#### 角色管理接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/v1/roles | 角色列表 |
| GET | /api/v1/roles/{id} | 角色详情 |
| POST | /api/v1/roles | 创建角色 |
| PUT | /api/v1/roles/{id} | 更新角色 |
| DELETE | /api/v1/roles/{id} | 删除角色 |
| GET | /api/v1/roles/{id}/menus | 获取角色菜单 |
| PUT | /api/v1/roles/{id}/menus | 分配角色菜单 |
| GET | /api/v1/roles/users/{userId} | 获取用户角色 |
| POST | /api/v1/roles/{roleId}/users/{userId} | 分配用户角色 |
| DELETE | /api/v1/roles/{roleId}/users/{userId} | 移除用户角色 |

#### 菜单管理接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/v1/menus | 菜单树 |
| GET | /api/v1/menus/{id} | 菜单详情 |
| POST | /api/v1/menus | 创建菜单 |
| PUT | /api/v1/menus/{id} | 更新菜单 |
| DELETE | /api/v1/menus/{id} | 删除菜单 |
| GET | /api/v1/menus/user | 获取当前用户菜单 |

#### 部门管理接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/v1/departments | 部门树 |
| GET | /api/v1/departments/{id} | 部门详情 |
| POST | /api/v1/departments | 创建部门 |
| PUT | /api/v1/departments/{id} | 更新部门 |
| DELETE | /api/v1/departments/{id} | 删除部门 |

#### 数据看板接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/v1/dashboard/overview | 概览统计 |
| GET | /api/v1/dashboard/project-stats | 项目统计 |
| GET | /api/v1/dashboard/test-stats | 测试统计 |
| GET | /api/v1/dashboard/trend | 趋势数据 |

#### AI工具箱接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/v1/ai/call-records | 调用记录列表 |
| GET | /api/v1/ai/call-records/{id} | 调用记录详情 |
| GET | /api/v1/ai/statistics | 统计分析 |
| GET | /api/v1/ai/trend | 趋势分析 |

## 六、前端路由设计

### 6.1 路由结构

```
/system                    # 系统管理
  /user                   # 用户管理
  /role                   # 角色管理
  /permission             # 权限管理
  /department             # 部门管理
  /operation-log          # 操作日志

/dashboard                # 数据看板
  /overview               # 概览
  /project                # 项目统计
  /test                   # 测试统计

/ai-toolkit              # AI工具箱
  /call-record           # 调用记录
  /statistics            # 统计分析
```

### 6.2 菜单与路由映射

菜单表中的 `path` 和 `component` 字段直接映射到前端路由配置。

## 七、工程化

### 7.1 项目结构

```
apps/
├── backend/
│   ├── auth-service/          # 认证服务
│   ├── user-service/          # 用户服务
│   │   ├── src/main/
│   │   │   ├── java/com/aitest/user/
│   │   │   │   ├── entity/    # 实体类
│   │   │   │   ├── mapper/    # MyBatis Mapper
│   │   │   │   ├── service/   # 服务层
│   │   │   │   ├── controller/# 控制器
│   │   │   │   ├── dto/       # 数据传输对象
│   │   │   │   └── config/    # 配置类
│   │   │   └── resources/
│   │   │       ├── mapper/    # Mapper XML
│   │   │       └── application.yml
│   │   └── pom.xml
│   ├── dashboard-service/     # 数据看板服务
│   └── common/                # 公共模块
└── frontend/                  # 前端应用
```

### 7.2 代码规范

1. **命名规范**: 使用驼峰命名法
2. **注释规范**: 关键业务逻辑必须添加中文注释
3. **异常处理**: 统一使用 GlobalExceptionHandler
4. **日志规范**: 使用 SLF4J + Logback

### 7.3 安全规范

1. **JWT认证**: 所有API必须通过JWT Token认证
2. **密码加密**: 使用BCrypt加密存储用户密码
3. **SQL注入防护**: 使用MyBatis参数化查询
4. **XSS防护**: 前端和后端双重防护
5. **敏感数据脱敏**: 日志、接口响应中的敏感数据脱敏

## 八、部署方案

### 8.1 Docker容器化

使用Docker Compose进行服务编排，包含：
- MySQL数据库
- Redis缓存
- 各微服务实例

### 8.2 环境配置

| 环境 | 配置文件 | 数据库 |
|------|---------|--------|
| 开发环境 | application-dev.yml | 本地MySQL |
| 测试环境 | application-test.yml | 测试MySQL |
| 生产环境 | application-prod.yml | 生产MySQL |

## 九、监控与运维

### 9.1 日志管理

- 使用ELK Stack（Elasticsearch + Logstash + Kibana）进行日志收集和分析
- 日志级别：DEBUG、INFO、WARN、ERROR

### 9.2 健康检查

- 各服务提供 `/actuator/health` 健康检查接口
- 使用Spring Boot Actuator监控服务状态

### 9.3 性能监控

- 使用Prometheus + Grafana进行性能监控
- 监控指标：QPS、响应时间、错误率

## 十、迭代计划

### 第一阶段（MVP）
- [x] 数据库设计与创建
- [ ] 用户管理功能
- [ ] 角色权限功能
- [ ] 基础数据看板
- [ ] AI调用记录管理

### 第二阶段（增强）
- [ ] 部门管理功能
- [ ] 数据级权限控制
- [ ] 高级数据看板
- [ ] 操作日志审计

### 第三阶段（优化）
- [ ] 性能优化
- [ ] 缓存优化
- [ ] 安全加固
- [ ] 监控告警完善
