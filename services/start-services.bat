@echo off
cd /d "%~dp0"
echo Disabling Docker BuildKit to fix gRPC issue...
set DOCKER_BUILDKIT=0
set COMPOSE_DOCKER_CLI_BUILD=0
echo Starting Docker services...
docker compose up -d
echo Services started. Check status with: docker compose ps