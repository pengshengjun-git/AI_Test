@echo off
setlocal
cd /d "%~dp0"

echo ============================================
echo  AI测试管理平台 - 本地 Maven 编译（可选）
echo  说明：Docker 镜像已在各 Dockerfile 内执行 mvn -jdk21 打包，
echo  若直接 docker compose build，通常无需先运行本脚本。
echo ============================================

echo.
echo 编译后端（需本机 JDK 21 + Maven）...
pushd ..\backend
call mvn clean package -DskipTests -q
if %errorlevel% neq 0 (
    echo Maven 编译失败。请使用 JDK 21，或跳过本脚本仅用 Docker 构建。
    popd
    exit /b 1
)
popd

echo.
echo ============================================
echo 编译完成。
echo 下一步在 docker 目录执行:
echo   cd /d "%~dp0"
echo   docker compose up -d --build
echo ============================================
pause
