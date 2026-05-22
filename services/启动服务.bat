
@echo off
chcp 65001 >nul
title AI测试管理平台 - 运维启动
echo ========================================
echo    AI测试管理平台 - Docker启动
echo ========================================
echo.

:: 设置环境变量
cd /d "%~dp0"
set DOCKER_BUILDKIT=0
set COMPOSE_DOCKER_CLI_BUILD=0

:: 检查Docker是否运行
echo [1/6] 检查Docker环境...
docker info >nul 2>&1
if errorlevel 1 (
    echo ❌ Docker未运行！请先启动Docker Desktop！
    echo.
    pause
    exit /b 1
)
echo ✅ Docker运行正常！
echo.

:: 清理旧容器
echo [2/6] 清理旧容器（可选）...
choice /c YN /m "是否清理旧容器和数据卷"
if errorlevel 2 (
    echo ⏭️ 跳过清理
) else (
    echo 正在清理...
    docker compose -f docker-compose.dev.yml down --remove-orphans
    echo ✅ 清理完成！
)
echo.

:: 拉取基础镜像（可选）
echo [3/6] 拉取基础镜像...
docker pull mysql:8.0.33 >nul 2>&1
docker pull redis:7-alpine >nul 2>&1
docker pull nginx:alpine >nul 2>&1
echo ✅ 基础镜像检查完成！
echo.

:: 启动服务
echo [4/6] 启动所有服务...
docker compose -f docker-compose.dev.yml up -d --build
if errorlevel 1 (
    echo ❌ 启动失败！请查看错误信息
    pause
    exit /b 1
)
echo ✅ 服务开始启动，请等待...
echo.

:: 等待服务启动
echo [5/6] 等待服务健康检查...
echo   （预计需要30-60秒）
timeout /t 45 /nobreak >nul
echo.

:: 检查服务状态
echo [6/6] 检查服务状态...
docker compose -f docker-compose.dev.yml ps
echo.

echo ========================================
echo    ✅ 部署完成！
echo ========================================
echo.
echo 🌐 访问地址：
echo   - 前端界面：http://localhost:80
echo   - 数据库：localhost:3308
echo   - Redis：localhost:6379
echo.
echo 🔐 登录账号：
echo   - 管理员：admin / admin123
echo   - 测试用户：testuser / test123
echo.
echo 📋 运维命令：
echo   - 查看日志：docker compose -f docker-compose.dev.yml logs -f
echo   - 查看状态：docker compose -f docker-compose.dev.yml ps
echo   - 停止服务：docker compose -f docker-compose.dev.yml down
echo.
echo 📌 下一步：
echo   1. 等待服务完全启动（约1-2分钟）
echo   2. 浏览器打开 http://localhost:80 验证功能
echo   3. 用admin/admin123登录
echo.
pause
