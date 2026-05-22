# API接口清单

本文档列出所有可用的API接口。

## 基础信息

- **Base URL**: `/api/v1`
- **认证方式**: Bearer Token (JWT)
- **响应格式**: JSON

## 通用响应格式

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

---

## 1. 认证服务 (auth-service: 8001)

### 1.1 用户登录
- **URL**: `POST /api/v1/auth/login`
- **请求体**:
```json
{
  "username": "admin",
  "password": "123456"
}
```
- **响应**:
```json
{
  "code": 200,
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "user": {
      "id": 1,
      "username": "admin",
      "email": "admin@example.com"
    }
  }
}
```

### 1.2 用户注册
- **URL**: `POST /api/v1/auth/register`
- **请求体**:
```json
{
  "username": "newuser",
  "password": "123456",
  "email": "newuser@example.com"
}
```

### 1.3 获取用户信息
- **URL**: `GET /api/v1/user/info`
- **Headers**: `Authorization: Bearer {token}`

### 1.4 用户登出
- **URL**: `POST /api/v1/auth/logout`
- **Headers**: `Authorization: Bearer {token}`

---

## 2. 用户服务 (user-service: 8002)

### 2.1 获取用户列表
- **URL**: `GET /api/v1/users?page=1&size=10`

### 2.2 获取用户详情
- **URL**: `GET /api/v1/users/{id}`

### 2.3 创建用户
- **URL**: `POST /api/v1/users`

### 2.4 更新用户
- **URL**: `PUT /api/v1/users/{id}`

### 2.5 删除用户
- **URL**: `DELETE /api/v1/users/{id}`

---

## 3. 项目服务 (project-service: 8003)

### 3.1 获取项目列表
- **URL**: `GET /api/v1/projects?page=1&size=10&name=&status=`

### 3.2 获取项目详情
- **URL**: `GET /api/v1/projects/{id}`

### 3.3 创建项目
- **URL**: `POST /api/v1/projects`
- **请求体**:
```json
{
  "name": "新项目",
  "code": "NEW-PROJ",
  "description": "项目描述",
  "status": "IN_PROGRESS"
}
```

### 3.4 更新项目
- **URL**: `PUT /api/v1/projects/{id}`

### 3.5 删除项目
- **URL**: `DELETE /api/v1/projects/{id}`

---

## 4. 测试用例服务 (testcase-service: 8004)

### 4.1 获取用例列表
- **URL**: `GET /api/v1/testcases?page=1&size=10&title=&priority=&status=`

### 4.2 获取用例详情
- **URL**: `GET /api/v1/testcases/{id}`

### 4.3 创建用例
- **URL**: `POST /api/v1/testcases`
- **请求体**:
```json
{
  "title": "测试用例标题",
  "description": "用例描述",
  "priority": "P1",
  "type": "API",
  "projectId": 1
}
```

### 4.4 更新用例
- **URL**: `PUT /api/v1/testcases/{id}`

### 4.5 删除用例
- **URL**: `DELETE /api/v1/testcases/{id}`

### 4.6 执行用例
- **URL**: `POST /api/v1/testcases/{id}/execute`

### 4.7 AI生成用例
- **URL**: `POST /api/v1/testcases/generate`
- **请求体**:
```json
{
  "requirementId": 1,
  "count": 10
}
```

---

## 5. 缺陷服务 (defect-service: 8005)

### 5.1 获取缺陷列表
- **URL**: `GET /api/v1/defects?page=1&size=10&title=&priority=&status=`

### 5.2 获取缺陷详情
- **URL**: `GET /api/v1/defects/{id}`

### 5.3 创建缺陷
- **URL**: `POST /api/v1/defects`
- **请求体**:
```json
{
  "title": "缺陷标题",
  "description": "缺陷描述",
  "priority": "P1",
  "severity": "HIGH",
  "projectId": 1
}
```

### 5.4 更新缺陷
- **URL**: `PUT /api/v1/defects/{id}`

### 5.5 删除缺陷
- **URL**: `DELETE /api/v1/defects/{id}`

### 5.6 分配缺陷
- **URL**: `POST /api/v1/defects/{id}/assign`
- **请求体**:
```json
{
  "handler": "username"
}
```

---

## 6. 需求服务 (requirement-service: 8006)

### 6.1 获取需求列表
- **URL**: `GET /api/v1/requirements?page=1&size=10&title=&status=`

### 6.2 获取需求详情
- **URL**: `GET /api/v1/requirements/{id}`

### 6.3 创建需求
- **URL**: `POST /api/v1/requirements`

### 6.4 更新需求
- **URL**: `PUT /api/v1/requirements/{id}`

### 6.5 删除需求
- **URL**: `DELETE /api/v1/requirements/{id}`

### 6.6 AI解析需求
- **URL**: `POST /api/v1/requirements/{id}/parse`

### 6.7 上传需求文档
- **URL**: `POST /api/v1/requirements/upload`
- **Content-Type**: `multipart/form-data`

---

## 7. 工作台服务 (dashboard-service: 8007) ✨新增

### 7.1 获取统计数据
- **URL**: `GET /api/v1/dashboard/stats`
- **响应**:
```json
{
  "code": 200,
  "data": {
    "projectCount": 5,
    "testcaseCount": 128,
    "defectCount": 15,
    "aiGeneratedCount": 45
  }
}
```

### 7.2 获取最近项目
- **URL**: `GET /api/v1/dashboard/recent-projects`
- **响应**:
```json
{
  "code": 200,
  "data": [
    {
      "id": 1,
      "name": "电商平台v2.0",
      "status": "IN_PROGRESS",
      "createdAt": "2026-05-01 10:00:00"
    }
  ]
}
```

### 7.3 获取最近缺陷
- **URL**: `GET /api/v1/dashboard/recent-defects`

### 7.4 获取系统状态
- **URL**: `GET /api/v1/dashboard/system-status`
- **响应**:
```json
{
  "code": 200,
  "data": [
    {
      "name": "认证服务",
      "status": "online"
    },
    {
      "name": "项目服务",
      "status": "online"
    }
  ]
}
```

### 7.5 获取待办事项
- **URL**: `GET /api/v1/dashboard/todo-list`
- **响应**:
```json
{
  "code": 200,
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

### 7.6 切换待办状态
- **URL**: `PUT /api/v1/dashboard/todo/{id}/status`
- **请求体**:
```json
{
  "completed": true
}
```

---

## 8. 策略服务 (strategy-service: 8008) ✨新增

### 8.1 获取策略列表
- **URL**: `GET /api/v1/strategy?page=1&size=10&name=&type=&status=`
- **响应**:
```json
{
  "code": 200,
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
    "total": 10,
    "page": 1,
    "size": 10
  }
}
```

### 8.2 获取策略详情
- **URL**: `GET /api/v1/strategy/{id}`

### 8.3 创建策略
- **URL**: `POST /api/v1/strategy`
- **请求体**:
```json
{
  "name": "新策略",
  "type": "REGRESSION",
  "description": "策略描述",
  "priority": "P1",
  "projectIds": [1, 2],
  "caseScope": ["ALL"],
  "environments": ["TEST"],
  "scheduleEnabled": false
}
```

### 8.4 更新策略
- **URL**: `PUT /api/v1/strategy/{id}`

### 8.5 删除策略
- **URL**: `DELETE /api/v1/strategy/{id}`

### 8.6 切换策略状态
- **URL**: `PUT /api/v1/strategy/{id}/status`
- **请求体**:
```json
{
  "status": "DISABLED"
}
```

**策略类型**:
- `SMOKE`: 冒烟测试
- `REGRESSION`: 回归测试
- `PERFORMANCE`: 性能测试
- `SECURITY`: 安全测试
- `INTEGRATION`: 集成测试

**优先级**:
- `P0`: 最高优先级
- `P1`: 高优先级
- `P2`: 中优先级
- `P3`: 低优先级

**状态**:
- `ENABLED`: 启用
- `DISABLED`: 禁用

---

## 9. AI服务 (ai-service: 8010)

### 9.1 获取AI任务列表
- **URL**: `GET /api/v1/ai/tasks`

### 9.2 创建AI任务
- **URL**: `POST /api/v1/ai/tasks`

### 9.3 获取模型配置
- **URL**: `GET /api/v1/ai/model/config`

### 9.4 更新模型配置
- **URL**: `PUT /api/v1/ai/model/config`

### 9.5 AI生成测试用例
- **URL**: `POST /api/v1/ai/generate/testcases`

### 9.6 AI分析缺陷
- **URL**: `POST /api/v1/ai/analyze/defect`

### 9.7 AI生成报告
- **URL**: `POST /api/v1/ai/generate/report`

---

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未授权（Token无效或过期） |
| 403 | 禁止访问（权限不足） |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

---

## 认证说明

除登录和注册接口外，其他接口都需要在请求头中携带Token：

```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

Token通过登录接口获取，有效期为24小时。

---

**更新日期**: 2026-05-10  
**版本**: v1.0.0
