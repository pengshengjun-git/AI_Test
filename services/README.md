# Docker 部署指南

## 概述

本项目已配置完整的 Docker 容器化部署方案，采用微服务架构，支持一键启动所有服务。

## 目录结构

```
docker/
├── docker-compose.yml          # 主Docker Compose配置
├── docker-compose.mock-demo.yml  # 仅前端 + mock，无 Java/MySQL
├── build-all.bat              # Windows构建脚本
├── build-all.sh               # Linux/Mac构建脚本
├── .env                       # 环境变量配置
├── auth-service/
│   └── Dockerfile             # 认证服务镜像配置
├── user-service/
│   └── Dockerfile             # 用户服务镜像配置
├── project-service/
│   └── Dockerfile             # 项目服务镜像配置
├── testcase-service/
│   └── Dockerfile             # 测试用例服务镜像配置
├── defect-service/
│   └── Dockerfile             # 缺陷服务镜像配置
├── requirement-service/
│   └── Dockerfile             # 需求服务镜像配置
├── dashboard-service/
│   └── Dockerfile             # 工作台服务镜像配置
├── strategy-service/
│   └── Dockerfile             # 策略服务镜像配置
├── ai-service/
│   └── Dockerfile             # AI服务镜像配置
├── frontend/
│   ├── Dockerfile             # 生产前端（反代至各微服务 + ai-service）
│   └── Dockerfile.mock        # 演示：静态前端 + 全部 /api 走 mock-service
├── mock-service/
│   └── Dockerfile             # 独立 Mock HTTP（Express，默认 8020）
└── nginx/
    ├── nginx.conf             # 生产 Nginx 反向代理
    └── nginx-mock-unified.conf # Mock 演示：/api → mock-service
```

## 服务架构

本项目包含以下微服务：

| 服务 | 端口 | 说明 |
|------|------|------|
| MySQL | 3306 | 数据库 |
| Redis | 6379 | 缓存 |
| auth-service | 8001 | 认证服务 |
| user-service | 8002 | 用户服务 |
| project-service | 8003 | 项目服务 |
| testcase-service | 8004 | 测试用例服务 |
| defect-service | 8005 | 缺陷服务 |
| requirement-service | 8006 | 需求服务 |
| dashboard-service | 8007 | 工作台服务 |
| strategy-service | 8008 | 策略服务 |
| ai-service | 8010 | AI服务 (FastAPI) |
| frontend | 80 | 前端Web界面 (Nginx) |
| mock-service | 8020 | Mock服务（可选） |

## 环境要求

- Docker >= 24.x
- Docker Compose >= 2.x
- Maven >= 3.9.x (仅本地编译时需要)
- Java >= 21 (仅本地编译时需要)

> **注意**: Java 服务镜像使用 **多阶段构建**，镜像内包含 **JDK 21 + Maven**，因此无需在本机预先打包 jar 文件即可构建镜像。

## 快速开始

### 1. 构建所有镜像

首次构建会下载依赖并编译，耗时较长属正常现象。

```bash
# Windows
docker\build-all.bat

# Linux/Mac
chmod +x docker/build-all.sh
./docker/build-all.sh
```

或者直接使用 `docker compose` 构建并启动：

```bash
cd docker
docker compose up -d --build
```

### 2. 启动所有服务

```bash
cd docker
docker compose up -d
```

### 3. 访问应用

- **前端界面**: [http://localhost](http://localhost)
- **AI服务文档**: [http://localhost:8010/docs](http://localhost:8010/docs)
- **其他API**: 通过前端 Nginx 反向代理访问，例如 `http://localhost/api/v1/auth`

### 4. 查看日志

```bash
# 查看所有服务日志
docker compose logs -f

# 查看特定服务日志
docker compose logs -f frontend
docker compose logs -f auth-service
```

### 5. 停止服务

```bash
# 停止但保留数据
docker compose down

# 停止并删除数据卷（谨慎使用，会清除数据库数据）
docker compose down -v
```

## Mock 模式

开发时可以使用 Mock 服务，无需启动完整的后端微服务集群。

### 方式 A：仅启动 Mock 服务 (配合本地前端开发)

1. 启动 Mock 服务：
   ```bash
   cd docker
   docker compose --profile mock up -d mock-service
   ```
2. 配置前端环境变量 (`frontend/.env.local`)：
   ```env
   VITE_USE_MOCK=true
   ```
3. 启动前端开发服务器：
   ```bash
   cd frontend
   npm run dev
   ```

### 方式 B：纯 Mock 演示栈 (前端 + Mock，无数据库/Java)

使用 `docker-compose.mock-demo.yml` 启动一个轻量级演示环境。前端容器内的 Nginx 会将所有 `/api/` 请求转发到 `mock-service`。

```bash
cd docker
docker compose -f docker-compose.mock-demo.yml up -d --build
```

- **访问地址**: [http://localhost:8080](http://localhost:8080)
- **Mock API 直连**: [http://localhost:8020](http://localhost:8020)

### Mock 服务管理

- **健康检查**: `curl -s http://localhost:8020/health`
- **重置数据**: `curl -s -X POST http://localhost:8020/api/v1/__mock/reset`
- **配置延迟**: 在 `docker/.env` 中设置 `MOCK_DELAY_MS=500`

## 环境变量

可以在 `docker/.env` 文件中配置环境变量：

```env
# Mock延迟（毫秒）
MOCK_DELAY_MS=500

# 数据库密码
MYSQL_ROOT_PASSWORD=root
```

## 数据持久化

MySQL 和 Redis 数据通过 Docker Volume 持久化：

- `mysql_data`: MySQL 数据
- `redis_data`: Redis 数据

即使删除容器，数据依然保留。如需彻底清除数据，请执行 `docker compose down -v`。

## 网络配置

所有服务连接到自定义网络 `ai-test-network`，可以通过服务名（容器名）互相访问。

## 健康检查

MySQL 和 Redis 配置了健康检查，其他依赖数据库的服务会在它们就绪后启动，确保服务依赖顺序正确。

## 常见问题

### 1. 端口冲突

如果宿主机端口（如 80, 3306, 6379）被占用，可以修改 `docker-compose.yml` 中的端口映射：

```yaml
ports:
  - "8080:80"  # 将宿主机的8080映射到容器的80
```

### 2. 构建失败

- 确保 Docker Desktop 已启动。
- 分配足够的内存给 Docker（建议 8GB+）。
- 检查网络连接，确保能拉取基础镜像。

### 3. 服务无法连接

- 检查容器状态：`docker compose ps`
- 检查网络连接：`docker network inspect ai-test-network`
- 查看服务日志排查错误：`docker compose logs <service-name>`

### 4. 数据库初始化

首次启动时，MySQL 容器会自动执行 `backend/database/schema.sql` 和 `data.sql` 初始化数据库结构及种子数据。这可能需要 1-2 分钟。

## 开发模式

本地开发 Java 服务时，可以只启动基础设施（MySQL, Redis），然后在本机 IDE 中运行 Java 应用：

```bash
docker compose up -d mysql redis
```

前端开发可参考 [Mock 模式 - 方式 A](#方式-a仅启动-mock-服务-配合本地前端开发)。

## 迁移到云服务器

### 阿里云/腾讯云部署步骤

1. **创建云服务器**
   - 选择 CentOS 7/8 或 Ubuntu 20.04+
   - 配置安全组，开放端口：80, 443, 3306(可选), 6379(可选)

2. **安装 Docker**
   ```bash
   # CentOS
   yum install -y docker docker-compose
   systemctl start docker
   systemctl enable docker

   # Ubuntu
   apt-get update
   apt-get install -y docker.io docker-compose
   systemctl start docker
   systemctl enable docker
   ```

3. **上传代码**
   ```bash
   scp -r ai-test-platform root@your-server-ip:/opt/
   ```

4. **编译并启动**
   ```bash
   cd /opt/ai-test-platform/docker
   ./build-all.sh
   docker-compose up -d
   ```

5. **配置域名与 HTTPS（可选）**
   - 在云服务商控制台配置域名解析到服务器 IP
   - 更新 `nginx/nginx.conf` 添加 SSL 证书配置

## 生产部署建议

1. **安全性**: 修改默认数据库密码，不要将敏感信息硬编码在代码中。
2. **HTTPS**: 启用 HTTPS，配置 SSL 证书。
3. **监控**: 配置日志收集（如 ELK）和监控告警（如 Prometheus + Grafana）。
4. **高可用**: 使用外部托管的数据库和 Redis 服务，配置负载均衡。
5. **资源限制**: 在 `docker-compose.yml` 中为每个服务设置 CPU 和内存限制。