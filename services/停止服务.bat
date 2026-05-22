
@echo off
chcp 65001 >nul
title AI测试管理平台 - 停止服务
echo ========================================
echo    AI测试管理平台 - 停止服务
echo ========================================
echo.

cd /d "%~dp0"

:: 询问是否删除数据
echo ⚠️ 警告：选择"是"将删除所有容器和数据卷！
choice /c YN /m "是否删除所有数据卷（数据会丢失）"

if errorlevel 2 (
    echo ⏭️ 停止服务，保留数据...
    docker compose -f docker-compose.dev.yml down
) else (
    echo 🗑️  停止服务，删除数据...
    docker compose -f docker-compose.dev.yml down -v
)

echo.
echo ✅ 服务已停止！
echo.
pause
