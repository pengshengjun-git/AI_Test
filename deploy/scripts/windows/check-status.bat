@echo off
chcp 65001 >nul
echo ========================================
echo AI测试平台 - 服务状态检查
echo ========================================
echo.

echo [1] 检查Docker运行状态...
docker info >nul 2>&1
if errorlevel 1 (
    echo ❌ Docker未运行
    pause
    exit /b 1
)
echo ✅ Docker运行正常
echo.

echo [2] 查看所有容器状态...
docker compose ps
echo.

echo [3] 检查关键端口...
echo 正在检查端口占用情况...
netstat -ano | findstr ":8001 " >nul && echo ✅ auth-service (8001) 端口已监听 || echo ❌ auth-service (8001) 端口未监听
netstat -ano | findstr ":8007 " >nul && echo ✅ dashboard-service (8007) 端口已监听 || echo ❌ dashboard-service (8007) 端口未监听
netstat -ano | findstr ":8008 " >nul && echo ✅ strategy-service (8008) 端口已监听 || echo ❌ strategy-service (8008) 端口未监听
netstat -ano | findstr ":80 " >nul && echo ✅ frontend (80) 端口已监听 || echo ❌ frontend (80) 端口未监听
echo.

echo [4] 测试API接口...
echo 测试工作台服务...
curl -s http://localhost:8007/api/v1/dashboard/stats >nul 2>&1 && echo ✅ 工作台服务响应正常 || echo ❌ 工作台服务无响应
echo 测试策略服务...
curl -s http://localhost:8008/api/v1/strategy >nul 2>&1 && echo ✅ 策略服务响应正常 || echo ❌ 策略服务无响应
echo.

echo [5] 访问建议...
echo 前端界面: http://localhost
echo 工作台API: http://localhost:8007/api/v1/dashboard/stats
echo 策略API: http://localhost:8008/api/v1/strategy
echo.

echo ========================================
echo 查看日志命令: docker compose logs -f
echo 停止服务命令: docker compose down
echo ========================================
pause
