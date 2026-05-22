
@echo off
chcp 65001 >nul
title AI测试管理平台 - 状态检查
echo ========================================
echo    AI测试管理平台 - 运维检查
echo ========================================
echo.

cd /d "%~dp0"

echo [1/4] 检查Docker运行状态...
docker info >nul 2>&1
if errorlevel 1 (
    echo ❌ Docker未运行！
) else (
    echo ✅ Docker运行正常
)
echo.

echo [2/4] 检查容器运行状态...
docker compose -f docker-compose.dev.yml ps
echo.

echo [3/4] 检查最近日志（最后30行）...
echo -- 查看所有服务最新日志 --
docker compose -f docker-compose.dev.yml logs --tail=30
echo.

echo [4/4] 检查端口占用...
echo -- 检查本地端口 --
netstat -ano | findstr ":80 "
netstat -ano | findstr ":3308 "
netstat -ano | findstr ":6379 "
echo.

echo ========================================
echo    ✅ 检查完成！
echo ========================================
echo.
echo 📋 运维命令：
echo   - 查看实时日志：docker compose -f docker-compose.dev.yml logs -f
echo   - 重启服务：docker compose -f docker-compose.dev.yml restart
echo   - 停止服务：docker compose -f docker-compose.dev.yml down
echo.
pause
