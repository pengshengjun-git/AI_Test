#!/bin/bash
set -e
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

echo "============================================"
echo " AI测试管理平台 - 本地 Maven 编译（可选）"
echo " Docker 镜像已在 Dockerfile 内使用 JDK21+Maven 打包，"
echo " 一般可直接: docker compose up -d --build"
echo "============================================"

echo ""
echo "编译后端（需本机 JDK 21 + Maven）..."
cd "$SCRIPT_DIR/../backend"
mvn clean package -DskipTests -q

echo ""
echo "============================================"
echo "编译完成。下一步:"
echo "  cd \"$SCRIPT_DIR\""
echo "  docker compose up -d --build"
echo "============================================"
