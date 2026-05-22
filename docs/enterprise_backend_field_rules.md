# AI测试管理平台 - 企业级后台管理系统字段规则

## 目录
- [用户表字段规则](#用户表字段规则)
- [角色表字段规则](#角色表字段规则)
- [部门表字段规则](#部门表字段规则)
- [菜单表字段规则](#菜单表字段规则)
- [角色菜单关联表字段规则](#角色菜单关联表字段规则)
- [AI调用记录表字段规则](#ai调用记录表字段规则)

---

## 用户表字段规则

### 字段定义

| 字段名 | 数据类型 | 长度 | 必填 | 默认值 | 说明 | 验证规则 |
|-------|---------|------|------|-------|-----|---------|
| id | BIGINT | - | 是 | 自增 | 主键ID | - |
| username | VARCHAR | 64 | 是 | - | 用户名 | 1. 4-20个字符<br>2. 只能包含字母、数字、下划线<br>3. 唯一，不重复 |
| password_hash | VARCHAR | 256 | 是 | - | 密码哈希值 | 使用BCrypt加密 |
| email | VARCHAR | 128 | 是 | - | 邮箱 | 1. 符合邮箱格式<br>2. 唯一，不重复 |
| nickname | VARCHAR | 64 | 否 | - | 昵称 | 最多64个字符 |
| avatar | VARCHAR | 512 | 否 | - | 头像URL | 合法的URL格式 |
| phone | VARCHAR | 32 | 否 | - | 手机号 | 1. 符合手机号格式<br>2. 可选，唯一 |
| department_id | BIGINT | - | 否 | NULL | 部门ID | 外键关联 department 表 |
| status | TINYINT | - | 是 | 1 | 用户状态 | 0: 禁用<br>1: 正常<br>2: 待激活 |
| created_at | DATETIME | - | 是 | 当前时间 | 创建时间 | 自动生成，不可修改 |
| updated_at | DATETIME | - | 是 | 当前时间 | 更新时间 | 自动更新，不可修改 |
| deleted | TINYINT | - | 是 | 0 | 删除标记 | 0: 未删除<br>1: 已删除（软删除） |

### 业务规则
- **用户名**：一旦创建，不允许修改
- **密码**：首次登录强制修改；重置密码时生成随机密码并通知用户
- **状态**：禁用状态的用户无法登录
- **删除**：只支持软删除，不物理删除
- **邮箱**：用于接收通知、重置密码等

---

## 角色表字段规则

### 字段定义

| 字段名 | 数据类型 | 长度 | 必填 | 默认值 | 说明 | 验证规则 |
|-------|---------|------|------|-------|-----|---------|
| id | BIGINT | - | 是 | 自增 | 主键ID | - |
| name | VARCHAR | 64 | 是 | - | 角色名称 | 1. 2-20个字符<br>2. 唯一，不重复 |
| code | VARCHAR | 64 | 是 | - | 角色编码 | 1. 大写字母、下划线<br>2. 唯一，不重复 |
| description | VARCHAR | 512 | 否 | - | 角色描述 | 最多512个字符 |
| status | TINYINT | - | 是 | 1 | 状态 | 0: 禁用<br>1: 启用 |
| created_at | DATETIME | - | 是 | 当前时间 | 创建时间 | 自动生成，不可修改 |
| updated_at | DATETIME | - | 是 | 当前时间 | 更新时间 | 自动更新，不可修改 |
| deleted | TINYINT | - | 是 | 0 | 删除标记 | 0: 未删除<br>1: 已删除（软删除） |

### 业务规则
- **角色编码**：系统预设角色的编码固定，不允许修改
- **删除限制**：有关联用户的角色不允许删除
- **内置角色**：系统管理员角色不可删除、不可禁用

---

## 部门表字段规则

### 字段定义

| 字段名 | 数据类型 | 长度 | 必填 | 默认值 | 说明 | 验证规则 |
|-------|---------|------|------|-------|-----|---------|
| id | BIGINT | - | 是 | 自增 | 主键ID | - |
| parent_id | BIGINT | - | 否 | 0 | 父部门ID | 0表示根部门 |
| name | VARCHAR | 128 | 是 | - | 部门名称 | 1. 2-64个字符<br>2. 同一父部门下唯一 |
| code | VARCHAR | 64 | 是 | - | 部门编码 | 1. 大写字母、数字、下划线<br>2. 全局唯一 |
| leader_id | BIGINT | - | 否 | NULL | 部门负责人ID | 外键关联 user 表 |
| sort_order | INT | - | 是 | 0 | 排序 | 数字越小越靠前 |
| description | VARCHAR | 512 | 否 | - | 部门描述 | 最多512个字符 |
| status | TINYINT | - | 是 | 1 | 状态 | 0: 禁用<br>1: 启用 |
| created_at | DATETIME | - | 是 | 当前时间 | 创建时间 | 自动生成，不可修改 |
| updated_at | DATETIME | - | 是 | 当前时间 | 更新时间 | 自动更新，不可修改 |
| deleted | TINYINT | - | 是 | 0 | 删除标记 | 0: 未删除<br>1: 已删除（软删除） |

### 业务规则
- **树形结构**：部门支持多层级树形结构
- **删除限制**：有子部门或关联用户的部门不允许删除
- **负责人**：部门负责人必须是本部门或上级部门的用户

---

## 菜单表字段规则

### 字段定义

| 字段名 | 数据类型 | 长度 | 必填 | 默认值 | 说明 | 验证规则 |
|-------|---------|------|------|-------|-----|---------|
| id | BIGINT | - | 是 | 自增 | 主键ID | - |
| parent_id | BIGINT | - | 否 | 0 | 父菜单ID | 0表示根菜单 |
| name | VARCHAR | 128 | 是 | - | 菜单名称 | 1. 2-64个字符<br>2. 同一父菜单下唯一 |
| code | VARCHAR | 128 | 是 | - | 菜单编码 | 1. 字母、下划线<br>2. 全局唯一 |
| path | VARCHAR | 256 | 否 | - | 前端路由路径 | 以 / 开头，相对路径 |
| component | VARCHAR | 256 | 否 | - | 前端组件路径 | 相对路径 |
| icon | VARCHAR | 128 | 否 | - | 图标 | Element Plus 图标名称 |
| type | VARCHAR | 32 | 是 | menu | 菜单类型 | directory: 目录<br>menu: 菜单<br>button: 按钮 |
| permission | VARCHAR | 256 | 否 | - | 权限标识 | 如 system:user:add |
| sort_order | INT | - | 是 | 0 | 排序 | 数字越小越靠前 |
| visible | TINYINT | - | 是 | 1 | 是否可见 | 0: 隐藏<br>1: 显示 |
| cacheable | TINYINT | - | 是 | 1 | 是否缓存 | 0: 不缓存<br>1: 缓存 |
| redirect | VARCHAR | 256 | 否 | - | 重定向路径 | 菜单类型为 directory 时使用 |
| description | VARCHAR | 512 | 否 | - | 描述 | 最多512个字符 |
| created_at | DATETIME | - | 是 | 当前时间 | 创建时间 | 自动生成，不可修改 |
| updated_at | DATETIME | - | 是 | 当前时间 | 更新时间 | 自动更新，不可修改 |
| deleted | TINYINT | - | 是 | 0 | 删除标记 | 0: 未删除<br>1: 已删除（软删除） |

### 业务规则
- **树形结构**：菜单支持多层级树形结构
- **类型约束**：
  - directory：只有 name、icon、redirect，不需要 path、component
  - menu：需要 path、component
  - button：需要 permission，用于权限控制
- **删除限制**：有子菜单或关联角色的菜单不允许删除
- **内置菜单**：系统预设菜单不可删除

---

## 角色菜单关联表字段规则

### 字段定义

| 字段名 | 数据类型 | 长度 | 必填 | 默认值 | 说明 | 验证规则 |
|-------|---------|------|------|-------|-----|---------|
| id | BIGINT | - | 是 | 自增 | 主键ID | - |
| role_id | BIGINT | - | 是 | - | 角色ID | 外键关联 role 表 |
| menu_id | BIGINT | - | 是 | - | 菜单ID | 外键关联 menu 表 |
| created_at | DATETIME | - | 是 | 当前时间 | 创建时间 | 自动生成，不可修改 |

### 业务规则
- **唯一性**：role_id + menu_id 组合唯一，避免重复关联
- **级联删除**：角色或菜单删除时，对应的关联记录自动删除
- **权限继承**：如果选中父菜单，子菜单可选但不强制选中

---

## AI调用记录表字段规则

### 字段定义

| 字段名 | 数据类型 | 长度 | 必填 | 默认值 | 说明 | 验证规则 |
|-------|---------|------|------|-------|-----|---------|
| id | BIGINT | - | 是 | 自增 | 主键ID | - |
| user_id | BIGINT | - | 是 | - | 用户ID | 外键关联 user 表 |
| project_id | BIGINT | - | 否 | NULL | 项目ID | 外键关联 project 表 |
| function_type | VARCHAR | 64 | 是 | - | 功能类型 | requirement_analysis: 需求分析<br>case_generate: 用例生成<br>defect_analysis: 缺陷分析<br>report_generate: 报告生成<br>risk_analysis: 风险分析 |
| model_name | VARCHAR | 128 | 是 | - | 模型名称 | 如 gpt-4、deepseek-chat 等 |
| prompt_tokens | INT | - | 是 | 0 | Prompt Token数 | 非负整数 |
| completion_tokens | INT | - | 是 | 0 | Completion Token数 | 非负整数 |
| total_tokens | INT | - | 是 | 0 | 总Token数 | 非负整数 |
| status | VARCHAR | 32 | 是 | - | 调用状态 | success: 成功<br>failed: 失败 |
| error_message | TEXT | - | 否 | NULL | 错误信息 | 失败时记录 |
| response_time | BIGINT | - | 否 | NULL | 响应时间（毫秒） | 非负整数 |
| request_id | VARCHAR | 128 | 否 | NULL | 请求ID | 用于追踪，唯一 |
| input_data | TEXT | - | 否 | NULL | 输入数据 | JSON格式存储 |
| output_data | TEXT | - | 否 | NULL | 输出数据 | JSON格式存储 |
| cost | DECIMAL | 10,6 | 是 | 0 | 调用成本 | 单位：美元，保留6位小数 |
| ip_address | VARCHAR | 64 | 否 | NULL | 客户端IP地址 | IP地址格式 |
| created_at | DATETIME | - | 是 | 当前时间 | 创建时间 | 自动生成，不可修改 |

### 业务规则
- **成本计算**：根据模型单价和Token使用量自动计算成本
- **数据脱敏**：input_data 和 output_data 中的敏感信息需要脱敏
- **数据归档**：超过一定时间（如180天）的数据自动归档到历史表
- **查询限制**：普通用户只能查看自己的调用记录
- **统计维度**：支持按用户、项目、模型、功能类型、时间范围等维度统计

---

## 通用字段规则

### 时间字段
- **created_at**：记录创建时间，插入时自动设置，之后不可修改
- **updated_at**：记录更新时间，每次更新时自动设置为当前时间

### 删除标记
- **deleted**：软删除标记，0表示正常，1表示已删除
- 查询时自动过滤 deleted = 1 的记录

### 状态字段
- 0 通常表示禁用/无效
- 1 通常表示启用/有效

### ID字段
- 统一使用 BIGINT 类型
- 使用数据库自增主键
- 不允许修改

---

## 数据校验规则

### 字符串格式校验
- **邮箱**：^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$
- **手机号**：^1[3-9]\d{9}$（中国大陆）
- **URL**：^https?://[^\s]+$

### 长度限制
- **编码类字段**：通常使用 VARCHAR(64)
- **名称类字段**：通常使用 VARCHAR(64-128)
- **描述类字段**：通常使用 VARCHAR(512)
- **路径类字段**：通常使用 VARCHAR(256)
- **大文本**：使用 TEXT 类型

### 数值范围
- **Token数**：非负整数
- **成本**：非负小数，保留6位小数
- **排序**：整数，通常范围 0-9999

---

## 索引规则

### 必须创建的索引
| 表名 | 索引字段 | 索引类型 | 说明 |
|-----|---------|---------|-----|
| user | username | UNIQUE | 用户名唯一索引 |
| user | email | UNIQUE | 邮箱唯一索引 |
| user | department_id | INDEX | 部门ID索引 |
| role | code | UNIQUE | 角色编码唯一索引 |
| department | code | UNIQUE | 部门编码唯一索引 |
| department | parent_id | INDEX | 父部门索引 |
| menu | code | UNIQUE | 菜单编码唯一索引 |
| menu | parent_id | INDEX | 父菜单索引 |
| role_menu | role_id, menu_id | UNIQUE | 角色菜单唯一索引 |
| ai_call_record | user_id | INDEX | 用户ID索引 |
| ai_call_record | project_id | INDEX | 项目ID索引 |
| ai_call_record | created_at | INDEX | 创建时间索引 |

### 查询优化索引
- 高频查询字段需要加索引
- 外键字段建议加索引
- 时间范围查询字段建议加索引
