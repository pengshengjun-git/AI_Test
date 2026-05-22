# 项目架构规范

## 项目信息
- 项目名称：AI测试管理平台
- 技术栈：Java + Python + Vue3
- 架构模式：前后端分离 + 微服务
- 核心框架：Spring Boot 3.x + FastAPI + Vue3

## 目录结构
```
project/
├── apps/
│   ├── backend/          # Java后端微服务
│   │   ├── auth-service/
│   │   ├── user-service/
│   │   ├── project-service/
│   │   ├── testcase-service/
│   │   ├── defect-service/
│   │   ├── requirement-service/
│   │   ├── dashboard-service/
│   │   ├── strategy-service/
│   │   └── common/
│   ├── ai-service/       # Python AI服务
│   │   ├── main.py
│   │   ├── platform_bff.py
│   │   ├── agents.py
│   │   ├── llm_client.py
│   │   └── requirements.txt
│   └── frontend/         # Vue3前端
│       └── src/
├── services/             # Docker服务配置
├── deploy/               # 部署配置
├── docs/                 # 项目文档
├── memory/               # 项目记忆（AI系统）
├── .trae/                # Trae IDE配置
└── config/               # 配置文件
```

## 模块边界

| 模块 | 职责 | 技术选型 | 端口 |
|------|------|---------|------|
| 前端 | 用户界面、交互逻辑 | Vue3 + Element Plus + TypeScript | 80 |
| 认证服务 | 用户认证、JWT | Java + Spring Boot 3.x | 8001 |
| 用户服务 | 用户管理、权限 | Java + Spring Boot 3.x | 8002 |
| 项目服务 | 项目管理、成员 | Java + Spring Boot 3.x | 8003 |
| 用例服务 | 测试用例管理 | Java + Spring Boot 3.x | 8004 |
| 缺陷服务 | 缺陷管理 | Java + Spring Boot 3.x | 8005 |
| 需求服务 | 需求文档管理 | Java + Spring Boot 3.x | 8006 |
| 工作台服务 | 数据看板、统计 | Java + Spring Boot 3.x | 8007 |
| 策略服务 | 测试策略管理 | Java + Spring Boot 3.x | 8008 |
| **AI服务** | **AI能力中心** | **Python + FastAPI + LangChain** | **8010** |
| MySQL | 数据存储 | MySQL 8.0.33 | 3306 |
| Redis | 缓存服务 | Redis 7.x | 6379 |

## 技术规范

### 前端
- 框架：Vue 3 + TypeScript + Vite
- UI组件库：Element Plus + Tailwind CSS
- 状态管理：Vue 3 Composition API
- 路由：Vue Router
- HTTP客户端：Axios

### Java后端
- 语言：Java 17
- 框架：Spring Boot 3.x
- ORM：MyBatis Plus
- 认证：JWT
- 数据库：MySQL 8.0.33
- 缓存：Redis 7.x

### Python AI服务
- 语言：Python 3.11
- Web框架：FastAPI 0.109.0
- 服务器：Uvicorn 0.27.0
- AI框架：LangChain 0.1.4
- LLM集成：OpenAI API 1.10.0
- 数据验证：Pydantic 2.5.3

## AI服务（Python）详细说明

### 概述
AI服务是平台的核心AI能力中心，使用Python实现，提供AI驱动的测试用例生成、需求解析、缺陷分析等功能。

### 主要功能模块

| 模块 | 文件 | 说明 |
|------|------|------|
| 主程序 | main.py | FastAPI服务入口，健康检查 |
| 平台BFF | platform_bff.py | 提供Dashboard、Strategy、AI三大模块API |
| AI智能体 | agents.py | 测试Agent、分析Agent等智能体实现 |
| LLM客户端 | llm_client.py | 大模型API统一封装 |
| 任务管理 | task_manager.py | 异步任务调度与状态管理 |
| 配置管理 | config.py | AI服务配置管理 |

### API路径前缀
- /api/v1/dashboard - 工作台接口
- /api/v1/strategy - 策略管理接口
- /api/v1/ai - AI能力接口

### 功能特性

#### 1. 测试用例生成
- 基于需求文档自动生成测试用例
- 支持用例优先级、风险评分、标签等元数据
- 包含前置条件、测试步骤、预期结果

#### 2. 需求解析
- 解析需求文档，提取功能点
- 识别业务规则、边界条件
- 分析风险点和测试点

#### 3. 缺陷分析
- 自动分析缺陷原因
- 提供修复建议
- 识别相似缺陷

#### 4. 报告生成
- 自动化测试报告生成
- 支持多格式输出

### 服务依赖
- 可独立运行（内置Mock数据）
- 可连接MySQL和Redis获取真实数据

### 部署配置
- Dockerfile：services/ai-service/Dockerfile
- 服务配置：deploy/docker-compose.yml（第256-266行）

## 核心功能模块

### 1. 用户管理
- 注册、登录、权限控制
- 角色、权限管理

### 2. 项目管理
- 项目创建、编辑、归档
- 项目成员管理
- 项目统计数据

### 3. 测试用例管理
- 用例创建、编辑、执行
- 用例分类、标签、优先级
- AI自动生成测试用例

### 4. 缺陷管理
- 缺陷创建、跟踪、修复
- 缺陷优先级、严重性
- AI缺陷智能分析

### 5. 需求管理
- 需求文档管理
- 需求解析和测试点提取
- AI需求智能解析

### 6. 工作台
- 数据看板
- 待办事项
- 系统状态监控

### 7. AI中心
- AI任务管理
- 模型配置
- AI能力调用

## 部署架构
- 开发环境：本地Docker Compose
- 测试环境：Docker容器
- 生产环境：Kubernetes + 云服务

## 维护责任人
- 系统架构师：整体架构决策
- 后端开发：Java API和业务逻辑
- Python开发：AI服务实现
- 前端开发：界面和交互

## 最后更新
- 创建日期：2024-01-15
- 更新日期：2026-05-22
- 更新内容：补充Python AI服务架构说明
