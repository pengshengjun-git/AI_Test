#!/bin/bash

echo "========================================"
echo "AI测试平台 - 快速启动脚本"
echo "========================================"
echo ""

echo "[1/3] 检查Docker状态..."
if ! docker info > /dev/null 2>&1; then
    echo "❌ Docker未运行，请先启动Docker"
    exit 1
fi
echo "✅ Docker运行正常"
echo ""

echo "[2/3] 构建并启动所有服务..."
cd docker
docker compose up -d
if [ $? -ne 0 ]; then
    echo "❌ Docker Compose启动失败"
    exit 1
fi
echo ""

echo "[3/3] 等待服务启动..."
sleep 10
echo ""

echo "========================================"
echo "✅ 所有服务已启动！"
echo "========================================"
echo ""
echo "访问地址:"
echo "  前端界面: http://localhost"
echo "  AI服务API文档: http://localhost:8010/docs"
echo ""
echo "服务端口:"
echo "  auth-service: 8001"
echo "  user-service: 8002"
echo "  project-service: 8003"
echo "  testcase-service: 8004"
echo "  defect-service: 8005"
echo "  requirement-service: 8006"
echo "  dashboard-service: 8007"
echo "  strategy-service: 8008"
echo "  ai-service: 8010"
echo ""
echo "查看日志: docker compose logs -f"
echo "停止服务: docker compose down"
echo ""
