# 功能变更文档

## 版本记录

| 日期 | 版本 | 变更内容 | 作者 |
|------|------|----------|------|
| 2026-05-11 | v1.0.0 | 初始版本 | System |

---

## 1. 数据展示修复

### 1.1 状态转换优化
- **文件**: `frontend/src/utils/statusMap.ts`
- **功能**: 统一状态转换工具函数，将英文状态转换为中文显示
- **支持状态**: 
  - `active` -> 进行中
  - `draft` -> 草稿
  - `pending` -> 待处理
  - `completed` -> 已完成
  - `待测试` -> 待测试
  - `测试通过` -> 测试通过
  - `测试失败` -> 测试失败

### 1.2 创建时间格式化
- **功能**: 统一日期时间显示格式为 `YYYY-MM-DD HH:mm:ss`
- **应用位置**: 所有列表页面的创建时间字段

---

## 2. 输入框长度限制

### 2.1 InputWithLimit 组件
- **文件**: `frontend/src/components/InputWithLimit.vue`
- **功能**: 统一输入框长度限制组件
- **参数**:
  - `maxlength`: 最大输入长度（默认100）
  - `type`: 输入类型（text/textarea）
  - `showCounter`: 是否显示字数统计
  - `showTip`: 是否显示超出提示

### 2.2 长度限制规则
| 字段类型 | 最大长度 | 应用位置 |
|----------|----------|----------|
| 普通输入框 | 100字符 | 标题、名称、模块等 |
| 文本域 | 500字符 | 描述、步骤、备注等 |
| 版本号 | 20字符 | 版本字段 |
| 人员名称 | 50字符 | 负责人、审核人等 |
| 关闭原因 | 200字符 | 关闭原因字段 |

---

## 3. 分页国际化

### 3.1 Element Plus 中文配置
- **文件**: `frontend/src/main.ts`
- **功能**: 配置Element Plus中文语言包
- **效果**: 分页组件显示中文格式（如"第1页/共5页"）

---

## 4. 用例管理字段扩展

### 4.1 新增字段
| 字段名 | 类型 | 必填 | 默认值 | 说明 |
|--------|------|------|--------|------|
| test_status | 枚举 | 是 | 待测试 | 测试状态：待测试/测试通过/测试失败 |
| test_module | 字符串 | 否 | - | 测试模块名称 |
| requirement_id | 数字 | 否 | - | 关联需求ID |
| project_id | 数字 | 否 | - | 关联项目ID |

### 4.2 测试状态枚举值
| 值 | 显示文本 | 标签类型 |
|----|----------|----------|
| 待测试 | 待测试 | info |
| 测试通过 | 测试通过 | success |
| 测试失败 | 测试失败 | danger |

---

## 5. 缺陷管理字段扩展

### 5.1 新增字段
| 字段名 | 类型 | 必填 | 默认值 | 说明 |
|--------|------|------|--------|------|
| requirement_id | 数字 | 否 | - | 关联需求ID |
| project_id | 数字 | 否 | - | 关联项目ID |

### 5.2 应用位置
- 新增缺陷表单
- 编辑缺陷表单
- 缺陷列表展示

---

## 6. 需求管理模块

### 6.1 模块概述
- **路径**: `/requirement`
- **功能**: 完整的需求管理CRUD功能

### 6.2 需求字段清单
| 字段名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | 数字 | 是 | 需求ID（自动生成） |
| name | 字符串 | 是 | 需求名称（最大100字符） |
| description | 字符串 | 否 | 需求描述（最大500字符） |
| type | 枚举 | 是 | 需求类型 |
| priority | 枚举 | 是 | 优先级 |
| source | 枚举 | 否 | 需求来源 |
| proposer | 字符串 | 否 | 提出人 |
| proposed_at | 日期 | 否 | 提出时间 |
| effective_version | 字符串 | 否 | 生效版本 |
| acceptance_criteria | 字符串 | 否 | 验收标准 |
| project_id | 数字 | 否 | 关联项目ID |
| owner | 字符串 | 否 | 负责人 |
| reviewer | 字符串 | 否 | 审核人 |
| permission_scope | 字符串 | 否 | 权限范围 |
| status | 枚举 | 是 | 需求状态 |
| review_result | 枚举 | 否 | 评审结果 |
| review_comments | 字符串 | 否 | 评审意见 |
| online_time | 日期 | 否 | 上线时间 |
| close_reason | 字符串 | 否 | 关闭原因 |

### 6.3 枚举值定义

**需求类型**:
- `feature` - 功能需求
- `non_feature` - 非功能需求
- `bug_fix` - Bug修复
- `tech_debt` - 技术债务

**优先级**:
- `P0` - 紧急
- `P1` - 高
- `P2` - 中
- `P3` - 低

**需求来源**:
- `internal` - 内部需求
- `customer` - 客户需求
- `market` - 市场调研
- `tech` - 技术改进

**需求状态**:
- `draft` - 草稿
- `pending` - 待评审
- `approved` - 已批准
- `in_progress` - 进行中
- `completed` - 已完成
- `rejected` - 已拒绝
- `closed` - 已关闭

**评审结果**:
- `approved` - 通过
- `rejected` - 拒绝
- `pending` - 待定

### 6.4 超长内容气泡提示
- **触发条件**: 内容超过10字符
- **功能**: 鼠标悬停显示完整内容气泡提示
- **组件**: `frontend/src/components/TooltipText.vue`

---

## 7. 测试计划管理模块

### 7.1 模块概述
- **路径**: `/testplan`
- **功能**: 测试计划的创建、编辑、查看、删除

### 7.2 测试计划字段
| 字段名 | 类型 | 必填 | 默认值 | 说明 |
|--------|------|------|--------|------|
| id | 数字 | 是 | 自动生成 | 计划ID |
| name | 字符串 | 是 | - | 计划名称 |
| description | 字符串 | 否 | - | 计划描述 |
| project_id | 数字 | 否 | - | 关联项目ID |
| status | 枚举 | 是 | draft | 计划状态 |
| start_date | 日期 | 否 | - | 开始时间 |
| end_date | 日期 | 否 | - | 结束时间 |
| owner | 字符串 | 否 | - | 负责人 |

### 7.3 计划状态
| 值 | 显示文本 |
|----|----------|
| draft | 草稿 |
| executing | 执行中 |
| completed | 已完成 |
| cancelled | 已取消 |

---

## 8. 测试覆盖率模块

### 8.1 模块概述
- **路径**: `/coverage`
- **功能**: 展示测试覆盖率统计数据和趋势

### 8.2 覆盖率统计指标
| 指标 | 说明 |
|------|------|
| 总代码行数 | 项目代码总行数 |
| 已覆盖行数 | 测试覆盖到的代码行数 |
| 覆盖率 | 已覆盖行数 / 总行数 × 100% |

### 8.3 页面布局
- **顶部统计卡片**: 展示核心覆盖率指标
- **趋势图表**: 展示近期覆盖率变化趋势
- **分布展示**: 展示已覆盖/未覆盖代码比例
- **覆盖率列表**: 展示各项目/模块的覆盖率明细

### 8.4 覆盖率记录字段
| 字段名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | 数字 | 是 | 记录ID |
| project_id | 数字 | 是 | 项目ID |
| covered_lines | 数字 | 是 | 已覆盖行数 |
| total_lines | 数字 | 是 | 总行数 |
| coverage_rate | 数字 | 是 | 覆盖率(%) |
| report_date | 日期 | 是 | 报告日期 |

---

## 9. 导航菜单更新

### 9.1 新增菜单项
| 菜单名称 | 路径 | 图标 |
|----------|------|------|
| 需求管理 | `/requirement` | - |
| 测试计划 | `/testplan` | - |
| 覆盖率 | `/coverage` | - |

### 9.2 菜单顺序
1. 工作台
2. 项目管理
3. 需求管理
4. 用例管理
5. 测试计划
6. 缺陷管理
7. 覆盖率
8. AI中心
9. 策略管理

---

## 10. 后端API扩展

### 10.1 用例管理API
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/v1/testcases` | 获取用例列表 |
| GET | `/api/v1/testcases/:id` | 获取用例详情 |
| POST | `/api/v1/testcases` | 新增用例（支持新字段） |
| PUT | `/api/v1/testcases/:id` | 更新用例（支持新字段） |
| DELETE | `/api/v1/testcases/:id` | 删除用例 |

### 10.2 缺陷管理API
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/v1/defects` | 获取缺陷列表 |
| GET | `/api/v1/defects/:id` | 获取缺陷详情 |
| POST | `/api/v1/defects` | 新增缺陷（支持新字段） |
| PUT | `/api/v1/defects/:id` | 更新缺陷（支持新字段） |
| DELETE | `/api/v1/defects/:id` | 删除缺陷 |

### 10.3 需求管理API
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/v1/requirements` | 获取需求列表 |
| GET | `/api/v1/requirements/:id` | 获取需求详情 |
| POST | `/api/v1/requirements` | 新增需求 |
| PUT | `/api/v1/requirements/:id` | 更新需求 |
| DELETE | `/api/v1/requirements/:id` | 删除需求 |

### 10.4 测试计划API
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/v1/testplans` | 获取计划列表 |
| GET | `/api/v1/testplans/:id` | 获取计划详情 |
| POST | `/api/v1/testplans` | 新增计划 |
| PUT | `/api/v1/testplans/:id` | 更新计划 |
| DELETE | `/api/v1/testplans/:id` | 删除计划 |

### 10.5 测试覆盖率API
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/v1/coverage` | 获取覆盖率列表 |
| GET | `/api/v1/coverage/:id` | 获取覆盖率详情 |
| POST | `/api/v1/coverage` | 录入覆盖率 |
| PUT | `/api/v1/coverage/:id` | 更新覆盖率 |
| DELETE | `/api/v1/coverage/:id` | 删除覆盖率 |
| GET | `/api/v1/coverage/stats` | 获取覆盖率统计 |

---

## 11. 数据库表扩展

### 11.1 test_cases 表新增字段
| 字段名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| test_status | VARCHAR(20) | '待测试' | 测试状态 |
| test_module | VARCHAR(100) | NULL | 测试模块 |
| requirement_id | INT | NULL | 关联需求ID |
| project_id | INT | NULL | 关联项目ID |

### 11.2 defects 表新增字段
| 字段名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| requirement_id | INT | NULL | 关联需求ID |
| project_id | INT | NULL | 关联项目ID |

### 11.3 requirements 表（新建）
| 字段名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| id | INT | AUTO_INCREMENT | 主键 |
| name | VARCHAR(100) | - | 需求名称 |
| description | TEXT | NULL | 需求描述 |
| type | VARCHAR(20) | 'feature' | 需求类型 |
| priority | VARCHAR(10) | 'P2' | 优先级 |
| source | VARCHAR(20) | NULL | 需求来源 |
| proposer | VARCHAR(50) | NULL | 提出人 |
| proposed_at | DATETIME | CURRENT_TIMESTAMP | 提出时间 |
| effective_version | VARCHAR(20) | NULL | 生效版本 |
| acceptance_criteria | TEXT | NULL | 验收标准 |
| project_id | INT | NULL | 关联项目ID |
| owner | VARCHAR(50) | NULL | 负责人 |
| reviewer | VARCHAR(50) | NULL | 审核人 |
| permission_scope | VARCHAR(100) | NULL | 权限范围 |
| status | VARCHAR(20) | 'draft' | 需求状态 |
| review_result | VARCHAR(20) | NULL | 评审结果 |
| review_comments | TEXT | NULL | 评审意见 |
| online_time | DATETIME | NULL | 上线时间 |
| close_reason | VARCHAR(200) | NULL | 关闭原因 |
| created_at | DATETIME | CURRENT_TIMESTAMP | 创建时间 |
| updated_at | DATETIME | CURRENT_TIMESTAMP ON UPDATE | 更新时间 |
| deleted | TINYINT(1) | 0 | 删除标记 |

### 11.4 test_plans 表（新建）
| 字段名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| id | INT | AUTO_INCREMENT | 主键 |
| name | VARCHAR(100) | - | 计划名称 |
| description | TEXT | NULL | 计划描述 |
| project_id | INT | NULL | 关联项目ID |
| status | VARCHAR(20) | 'draft' | 计划状态 |
| start_date | DATETIME | NULL | 开始时间 |
| end_date | DATETIME | NULL | 结束时间 |
| owner | VARCHAR(50) | NULL | 负责人 |
| created_at | DATETIME | CURRENT_TIMESTAMP | 创建时间 |
| updated_at | DATETIME | CURRENT_TIMESTAMP ON UPDATE | 更新时间 |
| deleted | TINYINT(1) | 0 | 删除标记 |

### 11.5 test_coverage 表（新建）
| 字段名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| id | INT | AUTO_INCREMENT | 主键 |
| project_id | INT | - | 项目ID |
| covered_lines | INT | 0 | 已覆盖行数 |
| total_lines | INT | 0 | 总行数 |
| coverage_rate | DECIMAL(5,2) | 0 | 覆盖率 |
| report_date | DATE | CURRENT_DATE | 报告日期 |
| created_at | DATETIME | CURRENT_TIMESTAMP | 创建时间 |
| updated_at | DATETIME | CURRENT_TIMESTAMP ON UPDATE | 更新时间 |
| deleted | TINYINT(1) | 0 | 删除标记 |

---

## 12. 注意事项

### 12.1 输入验证
- 所有输入框均已添加长度限制
- 超出长度时显示红色警告提示
- 阻止继续输入超出部分

### 12.2 数据持久化
- 使用MySQL数据库存储所有数据
- 支持软删除（deleted字段标记）
- 所有表包含created_at和updated_at字段

### 12.3 国际化
- Element Plus组件已配置中文语言包
- 所有状态显示已转换为中文

### 12.4 响应式设计
- 所有页面均支持响应式布局
- 表格支持横向滚动
- 卡片自适应宽度