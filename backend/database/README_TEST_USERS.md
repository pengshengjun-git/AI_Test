# 测试用户初始化说明

## 测试用户列表

| 用户名 | 密码 | 角色 | 真实姓名 | 部门 |
|--------|------|------|----------|------|
| admin | Admin@123 | SUPER_ADMIN | System Admin | Tech Dept |
| manager | Admin@123 | PROJECT_MANAGER | Project Manager Zhang | PM Dept |
| tester1 | Admin@123 | TEST_ENGINEER | Test Engineer Li | QA Dept |
| tester2 | Admin@123 | TEST_ENGINEER | Test Engineer Wang | QA Dept |
| user1 | Admin@123 | USER | Normal User Zhao | Product Dept |
| user2 | Admin@123 | USER | Normal User Qian | Dev Dept |
| admin2 | Admin@123 | ADMIN | Admin Sun | Ops Dept |

## 使用方法

### 方法1：通过数据库管理工具执行

1. 打开你的数据库管理工具（如Navicat、DBeaver、MySQL Workbench等）
2. 连接到 `ai_test_platform` 数据库
3. 打开并执行 `init_test_users_simple.sql` 脚本

### 方法2：通过命令行执行

```bash
mysql -u root -p ai_test_platform < backend/database/init_test_users_simple.sql
```

### 方法3：Docker部署时自动初始化

Docker部署时，`deploy/init-db.sql` 已包含这些测试用户，会自动初始化。

## 验证用户创建成功

执行以下SQL查询查看所有用户：

```sql
SELECT id, username, real_name, email, phone, role, status, department_name, created_at 
FROM user 
ORDER BY id;
```

## 密码说明

所有测试用户密码统一为：**Admin@123**

密码BCrypt哈希值：`$2a$10$6w8hKHxv.cnYroA6RE6CMe8yF4GWhU1p5lWCRTSTqHdoCFzh5oq2q`

## 角色说明

- **SUPER_ADMIN**: 超级管理员，拥有所有权限
- **ADMIN**: 管理员，拥有大部分管理权限
- **PROJECT_MANAGER**: 项目经理，负责项目管理
- **TEST_ENGINEER**: 测试工程师，负责测试相关工作
- **USER**: 普通用户，基础访问权限
