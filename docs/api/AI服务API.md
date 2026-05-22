# AI服务API文档

## 概述

AI服务是平台的核心AI能力中心，基于Python + FastAPI实现，提供AI驱动的测试用例生成、需求解析、缺陷分析等功能。

- 服务地址：http://localhost:8010
- 文档位置：apps/ai-service/
- 部署配置：services/ai-service/Dockerfile

## 健康检查

### GET /health

检查服务健康状态

**响应示例**
```json
{
  "status": "healthy",
  "service": "ai-service",
  "timestamp": "2026-05-22T10:00:00"
}
```

---

## 通用响应格式

所有API遵循统一响应格式：

**成功响应**
```json
{
  "code": 0,
  "message": "success",
  "data": {}
}
```

**失败响应**
```json
{
  "code": 404,
  "message": "错误信息",
  "data": null
}
```

---

## Dashboard接口

### GET /api/v1/dashboard/stats

获取工作台统计数据

**响应示例**
```json
{
  "code": 0,
  "message": "success",
  "data": {
    "projectCount": 5,
    "testcaseCount": 128,
    "defectCount": 15,
    "aiGeneratedCount": 45
  }
}
```

### GET /api/v1/dashboard/recent-projects

获取最近项目列表

**响应示例**
```json
{
  "code": 0,
  "message": "success",
  "data": [
    {
      "id": 1,
      "name": "电商平台v2.0",
      "code": "ECOM-V2",
      "status": "IN_PROGRESS",
      "description": "电商平台升级项目",
      "createdAt": "2026-05-01 10:00:00"
    }
  ]
}
```

### GET /api/v1/dashboard/recent-defects

获取最近缺陷列表

**响应示例**
```json
{
  "code": 0,
  "message": "success",
  "data": [
    {
      "id": 1,
      "title": "登录接口返回数据异常",
      "priority": "P0",
      "severity": "CRITICAL",
      "status": "FIXING",
      "projectId": 1,
      "creator": "admin",
      "createdAt": "2026-05-08 10:00:00"
    }
  ]
}
```

### GET /api/v1/dashboard/system-status

获取系统状态

**响应示例**
```json
{
  "code": 0,
  "message": "success",
  "data": [
    {"name": "认证服务", "status": "online"},
    {"name": "项目服务", "status": "online"},
    {"name": "用例服务", "status": "online"},
    {"name": "AI服务", "status": "online"}
  ]
}
```

### GET /api/v1/dashboard/todo-list

获取待办事项列表

**响应示例**
```json
{
  "code": 0,
  "message": "success",
  "data": [
    {
      "id": 1,
      "title": "完成测试计划文档",
      "completed": false,
      "deadline": "2026-05-12"
    }
  ]
}
```

### PUT /api/v1/dashboard/todo/{todo_id}/status

切换待办事项完成状态

**路径参数**
- todo_id: 待办事项ID

**请求体**
```json
{
  "completed": true
}
```

---

## Strategy接口

### GET /api/v1/strategy

获取策略列表

**查询参数**
- name: 策略名称（可选）
- type: 策略类型（可选）
- status: 状态（可选）
- page: 页码，默认1
- size: 每页数量，默认10

**响应示例**
```json
{
  "code": 0,
  "message": "success",
  "data": {
    "list": [
      {
        "id": 1,
        "name": "核心功能冒烟测试",
        "type": "SMOKE",
        "description": "每次部署后对核心功能进行快速验证",
        "priority": "P0",
        "status": "ENABLED",
        "creator": "admin",
        "createTime": "2026-05-01 10:30:00"
      }
    ],
    "total": 3,
    "page": 1,
    "size": 10
  }
}
```

### GET /api/v1/strategy/{strategy_id}

获取策略详情

**路径参数**
- strategy_id: 策略ID

### POST /api/v1/strategy

创建策略

**请求体**
```json
{
  "name": "新策略",
  "type": "SMOKE",
  "description": "策略描述",
  "priority": "P1",
  "status": "ENABLED"
}
```

### PUT /api/v1/strategy/{strategy_id}

更新策略

**路径参数**
- strategy_id: 策略ID

### DELETE /api/v1/strategy/{strategy_id}

删除策略

### PUT /api/v1/strategy/{strategy_id}/status

切换策略状态

**请求体**
```json
{
  "status": "DISABLED"
}
```

---

## AI接口

### GET /api/v1/ai/tasks

获取AI任务列表

**响应示例**
```json
{
  "code": 0,
  "message": "success",
  "data": [
    {
      "id": 1,
      "name": "电商平台用例生成",
      "type": "CASE_GENERATE",
      "status": "COMPLETED",
      "progress": 100,
      "createTime": "2026-05-09 10:30:00"
    }
  ]
}
```

### POST /api/v1/ai/tasks

创建AI任务

**请求体**
```json
{
  "name": "新任务",
  "type": "CASE_GENERATE"
}
```

**任务类型**
- CASE_GENERATE - 测试用例生成
- REQUIREMENT_PARSE - 需求解析
- DEFECT_ANALYZE - 缺陷分析
- REPORT_GENERATE - 报告生成

### GET /api/v1/ai/tasks/{task_id}

获取任务状态

**路径参数**
- task_id: 任务ID

### GET /api/v1/ai/model/config

获取AI模型配置

**响应示例**
```json
{
  "code": 0,
  "message": "success",
  "data": {
    "modelName": "gpt-4",
    "temperature": 0.7,
    "maxTokens": 4096,
    "topP": 0.9
  }
}
```

### PUT /api/v1/ai/model/config

更新AI模型配置

**请求体**
```json
{
  "modelName": "gpt-4",
  "temperature": 0.8,
  "maxTokens": 8192
}
```

### POST /api/v1/ai/generate/testcases

AI生成测试用例

**请求体**
```json
{
  "projectId": 1,
  "requirement": "用户登录需求文档..."
}
```

**响应示例**
```json
{
  "code": 0,
  "message": "success",
  "data": {
    "task_id": "task_20260522100000",
    "status": "completed",
    "message": "AI生成测试用例成功",
    "projectId": 1,
    "test_cases": [
      {
        "title": "登录成功测试",
        "priority": "P1",
        "preconditions": ["用户已注册"],
        "steps": ["打开登录页", "输入合法账号密码", "点击登录"],
        "expected_result": "进入首页",
        "tags": ["登录"],
        "ai_generated": true,
        "risk_score": 0.85
      }
    ],
    "success": true,
    "count": 2
  }
}
```

### POST /api/v1/ai/parse/requirement

AI解析需求文档

**请求体**
```json
{
  "requirement": "用户登录需求文档..."
}
```

**响应示例**
```json
{
  "code": 0,
  "message": "success",
  "data": {
    "content": "用户登录需求文档...",
    "function_points": ["用户登录", "用户注册", "密码重置"],
    "business_rules": ["密码长度不少于8位", "用户名不能重复"],
    "boundary_conditions": ["密码为空", "用户名为空"],
    "risk_points": ["暴力破解风险"],
    "test_points": ["正常登录流程", "异常登录场景"]
  }
}
```

### POST /api/v1/ai/analyze/defect

AI分析缺陷

**请求体**
```json
{
  "defectId": 1,
  "defectDescription": "登录接口返回500错误"
}
```

**响应示例**
```json
{
  "code": 0,
  "message": "success",
  "data": {
    "analysis": "根据AI分析，此缺陷可能与边界条件处理不当有关。",
    "suggestions": ["检查边界条件", "添加单元测试", "复核并发场景"],
    "similar_defects": [],
    "root_cause": "代码逻辑问题",
    "risk_level": "medium",
    "fix_suggestions": ["检查边界条件", "添加单元测试"]
  }
}
```

### POST /api/v1/ai/generate/report

AI生成测试报告

**请求体**
```json
{
  "projectId": 1,
  "type": "weekly"
}
```

**响应示例**
```json
{
  "code": 0,
  "message": "success",
  "data": {
    "success": true,
    "message": "报告生成完成",
    "url": "/report/download/demo",
    "projectId": 1,
    "type": "weekly"
  }
}
```

---

## 兼容旧路径

为保持兼容性，以下旧路径仍然可用：

- POST /api/v1/ai/cases/generate - 已重定向到 /api/v1/ai/generate/testcases
- POST /api/v1/ai/requirements/parse - 已重定向到 /api/v1/ai/parse/requirement
- POST /api/v1/ai/defects/analyze - 已重定向到 /api/v1/ai/analyze/defect

---

## 文件结构

| 文件 | 说明 |
|-----|-----|
| main.py | 服务入口，健康检查 |
| platform_bff.py | BFF层，所有API实现 |
| agents.py | AI智能体实现 |
| llm_client.py | LLM客户端封装 |
| task_manager.py | 任务管理器 |
| config.py | 配置管理 |
| requirements.txt | Python依赖 |
