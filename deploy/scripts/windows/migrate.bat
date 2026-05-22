@echo off
setlocal enabledelayedexpansion

echo ================================================
echo AI测试管理平台 - 目录迁移脚本 (Windows)
echo ================================================

:: 创建新目录结构
echo [1/4] 创建新目录结构...

mkdir services\auth-service
mkdir services\user-service
mkdir services\project-service
mkdir services\testcase-service
mkdir services\defect-service
mkdir services\requirement-service
mkdir services\dashboard-service
mkdir services\strategy-service
mkdir services\ai-service
mkdir services\mock-service
mkdir services\frontend
mkdir services\nginx
mkdir services\gateway-service

mkdir ai-infra\ollama
mkdir ai-infra\open-webui
mkdir ai-infra\anythingllm
mkdir ai-infra\ragflow
mkdir ai-infra\n8n
mkdir ai-infra\one-api
mkdir ai-infra\postgres
mkdir ai-infra\redis
mkdir ai-infra\vector-db\chroma
mkdir ai-infra\vector-db\milvus
mkdir ai-infra\vector-db\qdrant

mkdir deploy\nginx\conf.d
mkdir deploy\nginx\ssl
mkdir deploy\scripts\windows
mkdir deploy\scripts\linux
mkdir deploy\scripts\powershell

mkdir data\mysql
mkdir data\redis
mkdir data\rabbitmq
mkdir data\postgres
mkdir data\ollama
mkdir data\open-webui
mkdir data\anythingllm
mkdir data\ragflow
mkdir data\n8n
mkdir data\chroma
mkdir data\milvus
mkdir data\qdrant

mkdir logs\backend
mkdir logs\frontend
mkdir logs\ai-service
mkdir logs\gateway
mkdir logs\mysql
mkdir logs\redis
mkdir logs\rabbitmq
mkdir logs\ollama
mkdir logs\open-webui
mkdir logs\anythingllm
mkdir logs\ragflow
mkdir logs\n8n
mkdir logs\system

mkdir monitoring\prometheus
mkdir monitoring\grafana
mkdir monitoring\loki
mkdir monitoring\alertmanager

mkdir docs\api
mkdir docs\architecture
mkdir docs\design
mkdir docs\requirements
mkdir docs\development
mkdir docs\deployment
mkdir docs\testing
mkdir docs\ai\prompts
mkdir docs\ai\rag
mkdir docs\ai\agents
mkdir docs\ai\workflows
mkdir docs\reports

mkdir assets\images
mkdir assets\diagrams
mkdir assets\videos
mkdir assets\templates

mkdir temp

mkdir workspace\uploads
mkdir workspace\generated
mkdir workspace\embeddings
mkdir workspace\exports

mkdir tests\unit
mkdir tests\integration
mkdir tests\performance
mkdir tests\security
mkdir tests\ai-eval

echo 目录结构创建完成！

:: 迁移后端服务
echo [2/4] 迁移后端服务...

xcopy /E /I /Y backend\auth-service apps\backend\auth-service
xcopy /E /I /Y backend\user-service apps\backend\user-service
xcopy /E /I /Y backend\project-service apps\backend\project-service
xcopy /E /I /Y backend\testcase-service apps\backend\testcase-service
xcopy /E /I /Y backend\defect-service apps\backend\defect-service
xcopy /E /I /Y backend\requirement-service apps\backend\requirement-service
xcopy /E /I /Y backend\dashboard-service apps\backend\dashboard-service
xcopy /E /I /Y backend\strategy-service apps\backend\strategy-service
xcopy /E /I /Y backend\common apps\backend\common
copy /Y backend\pom.xml apps\backend\pom.xml
xcopy /E /I /Y backend\database apps\backend\database

echo 后端服务迁移完成！

:: 迁移前端应用
echo [3/4] 迁移前端应用...

xcopy /E /I /Y frontend apps\frontend
xcopy /E /I /Y ai-service apps\ai-service

echo 前端应用迁移完成！

:: 迁移Docker配置
echo [4/4] 迁移Docker配置...

xcopy /E /I /Y docker\auth-service services\auth-service
xcopy /E /I /Y docker\user-service services\user-service
xcopy /E /I /Y docker\project-service services\project-service
xcopy /E /I /Y docker\testcase-service services\testcase-service
xcopy /E /I /Y docker\defect-service services\defect-service
xcopy /E /I /Y docker\requirement-service services\requirement-service
xcopy /E /I /Y docker\dashboard-service services\dashboard-service
xcopy /E /I /Y docker\strategy-service services\strategy-service
xcopy /E /I /Y docker\ai-service services\ai-service
xcopy /E /I /Y docker\mock-service services\mock-service
xcopy /E /I /Y docker\frontend services\frontend
xcopy /E /I /Y docker\nginx services\nginx

copy /Y docker\docker-compose.yml deploy\docker-compose.yml
copy /Y docker\docker-compose.dev.yml deploy\docker-compose.dev.yml
copy /Y docker\docker-compose.mock-demo.yml deploy\docker-compose.mock-demo.yml

copy /Y docker\start-services.bat deploy\scripts\windows\start.bat
copy /Y docker\build-all.bat deploy\scripts\windows\build-all.bat
copy /Y docker\build-all.sh deploy\scripts\linux\build-all.sh
copy /Y docker\pull-images.bat deploy\scripts\windows\pull-images.bat

copy /Y .env deploy\.env

echo Docker配置迁移完成！

:: 清理旧目录
echo 清理旧目录...
rmdir /S /Q backend
rmdir /S /Q frontend
rmdir /S /Q ai-service
rmdir /S /Q docker

echo ================================================
echo 迁移完成！
echo ================================================
echo 请更新 deploy/docker-compose.yml 中的路径引用
echo 启动命令: docker compose -f deploy/docker-compose.yml up -d
echo ================================================

endlocal
pause