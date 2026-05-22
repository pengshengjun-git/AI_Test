
@echo off
chcp 65001 >nul
title AI测试管理平台 - 一键启动
echo ========================================
echo    🚀 AI测试管理平台 - 一键启动
echo ========================================
echo.
echo 📌 说明：
echo   - 本脚本将自动启动所有Docker服务
echo   - 首次启动需要构建镜像，可能需要10-20分钟
echo   - 请确保Docker Desktop已启动
echo.
pause

cd /d "%~dp0deploy"
call 启动服务.bat
