# 代码风格规范

## 通用规范

### 缩进
- 使用 **4个空格** 缩进
- 禁止使用 Tab 字符

### 命名规范

#### 变量命名
- **Python**：snake_case（小写+下划线）
  - 示例：`user_name`, `order_list`, `is_active`
- **JavaScript/TypeScript**：camelCase（小写开头）
  - 示例：`userName`, `orderList`, `isActive`

#### 函数命名
- **Python**：snake_case，动词+名词
  - 示例：`get_user_info`, `create_order`, `validate_input`
- **JavaScript/TypeScript**：camelCase，动词+名词
  - 示例：`getUserInfo`, `createOrder`, `validateInput`

#### 常量命名
- 全部大写，下划线分隔
  - 示例：`MAX_RETRY_COUNT`, `DEFAULT_TIMEOUT`, `API_BASE_URL`

#### 类命名
- **Python**：PascalCase（大写开头）
  - 示例：`UserService`, `OrderController`, `BaseModel`
- **JavaScript/TypeScript**：PascalCase
  - 示例：`UserService`, `OrderController`, `VueComponent`

#### 文件命名
- **Python**：snake_case
  - 示例：`user_service.py`, `order_controller.py`
- **JavaScript/TypeScript**：kebab-case
  - 示例：`user-service.js`, `order-controller.vue`

## Python规范

### 导入顺序
1. 标准库
2. 第三方库
3. 本地应用/库

```python
# 标准库
import os
import json
from datetime import datetime

# 第三方库
import requests
from fastapi import APIRouter

# 本地
from models.user import UserModel
from utils.helper import format_date
```

### Docstring规范
所有公共模块、函数、类必须包含docstring：

```python
def get_user_by_id(user_id: int) -> dict:
    """
    根据用户ID获取用户信息

    Args:
        user_id (int): 用户ID

    Returns:
        dict: 用户信息字典，包含以下字段：
            - id: 用户ID
            - username: 用户名
            - email: 邮箱

    Raises:
        ValueError: 当user_id无效时
        UserNotFoundError: 当用户不存在时
    """
    pass
```

### 类型注解
必须使用类型注解：

```python
def process_data(
    name: str,
    age: int,
    scores: List[float]
) -> Dict[str, Any]:
    """处理用户数据"""
    pass
```

## JavaScript/TypeScript规范

### 导入导出
```javascript
// 命名导出
export const API_BASE_URL = 'http://localhost:8000';
export function getUserInfo() {}

// 默认导出
export default UserService;
```

### 注释规范
```javascript
/**
 * 获取用户信息
 * @param {number} userId - 用户ID
 * @returns {Promise<User>} 用户信息
 */
async function getUserInfo(userId) {
  // TODO: 添加错误处理
  const response = await api.get(`/users/${userId}`);
  return response.data;
}
```

### Vue组件规范
```vue
<template>
  <div class="user-profile">
    <h1>{{ userName }}</h1>
  </div>
</template>

<script>
export default {
  name: 'UserProfile',
  
  props: {
    userId: {
      type: Number,
      required: true
    }
  },
  
  data() {
    return {
      userName: ''
    }
  },
  
  methods: {
    async fetchUser() {
      // 实现逻辑
    }
  }
}
</script>

<style scoped>
.user-profile {
  padding: 20px;
}
</style>
```

## 数据库规范

### 表命名
- 使用snake_case
- 表名单数
- 示例：`user`, `test_case`, `execution_result`

### 字段命名
- 使用snake_case
- 示例：`user_id`, `created_at`, `is_deleted`

### SQL格式
```sql
SELECT 
    u.id,
    u.username,
    u.email,
    tc.name AS test_case_name
FROM users u
LEFT JOIN test_cases tc ON u.id = tc.user_id
WHERE u.is_active = 1
ORDER BY u.created_at DESC
LIMIT 10;
```

## Git提交规范

### 提交信息格式
```
<type>(<scope>): <subject>

<body>

<footer>
```

### Type类型
- feat：新功能
- fix：Bug修复
- docs：文档更新
- style：代码格式（不影响功能）
- refactor：重构
- test：测试相关
- chore：构建/工具相关

### 示例
```
feat(user): 添加用户注册功能

- 实现用户注册API
- 添加邮箱验证
- 优化注册流程

Closes #123
```

## 代码审查检查清单

### 功能性
- [ ] 功能实现完整
- [ ] 边界条件处理
- [ ] 错误处理完善
- [ ] 日志记录适当

### 规范性
- [ ] 命名符合规范
- [ ] 代码格式统一
- [ ] 注释完整清晰
- [ ] 类型注解齐全

### 性能
- [ ] 无明显性能问题
- [ ] 资源释放正确
- [ ] 数据库查询优化

### 安全
- [ ] 参数校验完整
- [ ] 权限控制正确
- [ ] 无安全漏洞

## 维护责任人
- 技术总监：规范制定和更新
- 所有开发人员：遵守和反馈

## 最后更新
- 创建日期：2024-01-15
- 技术总监
