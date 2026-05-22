# 🤖 AI测试管理平台

一个让测试工作更轻松的智能管理系统！

## 🚀 快速开始

### 一键启动（推荐）

直接双击项目根目录的 **start.bat** 脚本即可一键启动所有服务！

### 手动启动

```bash
# 进入deploy目录
cd deploy

# 启动所有服务
docker-compose up -d
```

### 访问地址

- **前端界面**：http://localhost
- **MySQL**：localhost:3306 (账号: root, 密码: root)
- **Redis**：localhost:6379

### 测试账号

- **管理员**：admin / admin123
- **测试用户**：test / test123

### 常用脚本

- **start.bat** - 一键启动所有服务
- **stop.bat** - 停止所有服务
- **logs.bat** - 查看实时日志

---

## ✨ 主要功能

- ✅ 用户登录认证
- ✅ 工作台（统计数据）
- ✅ 项目管理
- ✅ 测试用例管理（支持AI生成）
- ✅ 缺陷管理
- ✅ 需求管理
- ✅ 策略管理
- ✅ AI中心

---

## 🏗️ 项目架构

```
前端 (Vue3 + TypeScript)
    ↓
后端微服务 (Spring Boot)
    ↓
MySQL + Redis
```

### 项目结构

```
AI-Test-Platform/
├── apps/                    # 应用源码
│   ├── frontend/           # 前端应用 (Vue3)
│   ├── backend/            # 后端微服务
│   │   ├── auth-service/   # 认证服务
│   │   ├── user-service/   # 用户服务
│   │   ├── project-service/# 项目服务
│   │   ├── testcase-service/# 用例服务
│   │   ├── defect-service/ # 缺陷服务
│   │   ├── requirement-service/# 需求服务
│   │   ├── dashboard-service/# 仪表盘服务
│   │   └── strategy-service/# 策略服务
│   └── ai-service/         # AI服务 (Python)
├── services/               # 预编译服务包
├── deploy/                 # 部署配置
│   └── docker-compose.yml  # Docker编排
├── start.bat              # 一键启动脚本
├── stop.bat               # 停止服务脚本
└── logs.bat               # 查看日志脚本
```

---

## 📚 文档导航

| 文档 | 说明 |
|-----|-----|
| [1-快速开始.md](./docs/1-快速开始.md) | 10分钟上手 |
| [2-项目介绍.md](./docs/2-项目介绍.md) | 项目功能介绍 |
| [3-怎么用.md](./docs/3-怎么用.md) | 使用指南 |
| [4-技术说明.md](./docs/4-技术说明.md) | 技术文档 |
| [5-API接口文档.md](./docs/5-API接口文档.md) | 接口文档 |
| [6-开发进度.md](./docs/6-开发进度.md) | 开发进度 |

