# API接口契约

## 接口规范
- 协议：RESTful API
- 格式：JSON
- 认证：JWT Bearer Token
- 编码：UTF-8
- 版本：v1

## 基础路径
```
/api/v1/
```

## 认证接口

### 用户登录
- **方法**：POST
- **路径**：/auth/login
- **描述**：用户登录获取Token

**请求体**：
```json
{
  "username": "string",
  "password": "string"
}
```

**响应**：
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "token": "string",
    "user": {
      "id": "number",
      "username": "string",
      "role": "string"
    }
  }
}
```

### 用户注册
- **方法**：POST
- **路径**：/auth/register
- **描述**：用户注册

**请求体**：
```json
{
  "username": "string",
  "email": "string",
  "password": "string"
}
```

**响应**：
```json
{
  "code": 200,
  "message": "注册成功",
  "data": {
    "userId": "number"
  }
}
```

## 测试用例接口

### 获取测试用例列表
- **方法**：GET
- **路径**：/testcases
- **描述**：获取所有测试用例

**查询参数**：
- page：页码（默认1）
- pageSize：每页数量（默认10）
- status：筛选状态（可选）

**响应**：
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": "number",
    "list": [
      {
        "id": "number",
        "name": "string",
        "status": "string",
        "createTime": "string"
      }
    ]
  }
}
```

### 创建测试用例
- **方法**：POST
- **路径**：/testcases
- **描述**：创建新的测试用例

**请求体**：
```json
{
  "name": "string",
  "description": "string",
  "steps": ["string"],
  "expectedResult": "string"
}
```

**响应**：
```json
{
  "code": 200,
  "message": "创建成功",
  "data": {
    "id": "number"
  }
}
```

### 执行测试用例
- **方法**：POST
- **路径**：/testcases/{id}/execute
- **描述**：执行指定的测试用例

**响应**：
```json
{
  "code": 200,
  "message": "执行成功",
  "data": {
    "executionId": "string",
    "status": "string",
    "result": "object"
  }
}
```

## AI助手接口

### 智能推荐
- **方法**：POST
- **路径**：/ai/recommend
- **描述**：AI推荐测试用例

**请求体**：
```json
{
  "context": "string",
  "type": "string"
}
```

**响应**：
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "recommendations": [
      {
        "testcaseId": "number",
        "reason": "string",
        "confidence": "number"
      }
    ]
  }
}
```

## 请求/响应规范

### 请求头标准
```
Content-Type: application/json
Authorization: Bearer {token}
Accept: application/json
```

### 错误响应格式
```json
{
  "code": "number",
  "message": "string",
  "error": "string"
}
```

### 错误码定义
| 错误码 | 说明 |
|--------|------|
| 400 | 参数错误 |
| 401 | 未授权 |
| 403 | 权限不足 |
| 404 | 资源不存在 |
| 500 | 服务器错误 |

## 维护责任人
- 后端开发：接口定义和实现
- 前端开发：接口调用和对接
- 测试工程师：接口测试验证

## 最后更新
- 创建日期：2024-01-15
- 后端开发
