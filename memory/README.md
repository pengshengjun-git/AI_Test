# Memory 系统使用指南

## 概述
本目录包含项目长期记忆核心文件，所有AI角色必须优先读取。

## 📂 文档查找快速指南（所有AI角色必读）

### 🔍 查找文档优先级流程
1. **先查标准位置** - 参考 `docs/文档查找指南.md` 中的标准文档位置
2. **再用通配符搜索** - 使用 `Glob` 工具进行全局通配符查找
3. **最后才创建新文档** - 确保没有重复才创建

### 📖 必读文档
- **[文档查找指南.md](../docs/文档查找指南.md)** - 完整的文档查找手册，包含通配符模式和标准位置

### 🔧 常用通配符查找模式
```
查找测试用例：**/docs/testing/测试用例*.md, **/测试用例*.md
查找需求文档：**/docs/requirements/*.md, **/*需求*.md
查找API文档：**/docs/api/*.md, **/*API*.md, **/*接口*.md
查找技术文档：**/docs/architecture/*.md, **/docs/development/*.md
```

## 文件清单
| 文件 | 作用 | 维护责任人 |
|------|------|-----------|
| README.md | memory系统总览 | 文档工程师 |
| architecture.md | 技术架构规范 | 系统架构师 |
| api-contract.md | API接口契约 | 后端开发 |
| coding-style.md | 代码风格规范 | 技术总监 |
| decisions.md | 技术决策记录 | 系统架构师 |
| changelog.md | 变更记录 | 文档工程师 |
| known-issues.md | 已知问题 | 测试工程师 |

## 使用规则
1. 开发前必须读取相关memory文件
2. 决策变更必须同步更新memory
3. 禁止忽略历史架构规范
4. 所有AI角色必须严格遵循memory内容

## 维护周期
| 文件 | 更新时机 | 维护责任人 |
|------|---------|-----------|
| architecture.md | 架构变更时 | 系统架构师 |
| api-contract.md | 接口定义变更时 | 后端开发 |
| coding-style.md | 代码规范变更时 | 技术总监 |
| decisions.md | 技术决策时（实时） | 系统架构师 |
| changelog.md | 每周更新 | 文档工程师 |
| known-issues.md | 发现问题时（实时） | 测试工程师 |

## 自动创建机制

### 触发条件
当AI检测到以下情况时，必须自动创建memory文件：
1. 首次进入项目且 `/memory/` 目录不存在
2. memory文件缺失或不完整
3. 用户明确要求初始化项目

### 创建流程
```text
检测 → 提示 → 确认 → 创建 → 填充 → 完成
```

### 维护提醒
AI在以下情况必须主动提醒维护责任人：
- 架构变更时提醒更新 architecture.md
- 接口变更时提醒更新 api-contract.md
- 代码规范变更时提醒更新 coding-style.md
- 发现问题时提醒更新 known-issues.md

## 核心原则
- Memory First：所有开发必须优先遵循 memory 内容
- 变更留痕：任何决策必须记录到 decisions.md
- 可追溯：所有变更必须记录到 changelog.md
- 禁止遗忘：禁止忽略历史架构规范

## 最后更新
- 创建日期：2024-01-15
- 文档工程师
