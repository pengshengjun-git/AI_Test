@echo off
echo ========================================
echo AI Test Platform - Docker Deployment
echo ========================================
echo.

cd /d "%~dp0docker"

echo [Step 1] Clean old containers...
docker compose down --remove-orphans --volumes
echo.

echo [Step 2] Set environment variables...
set DOCKER_BUILDKIT=0
echo.

echo [Step 3] Build services one by one...
echo.

echo --- Building auth-service ---
docker compose build auth-service
if errorlevel 1 (
    echo auth-service build failed!
    pause
    exit /b 1
)
echo.

echo --- Building user-service ---
docker compose build user-service
if errorlevel 1 (
    echo user-service build failed!
    pause
    exit /b 1
)
echo.

echo --- Building project-service ---
docker compose build project-service
if errorlevel 1 (
    echo project-service build failed!
    pause
    exit /b 1
)
echo.

echo --- Building testcase-service ---
docker compose build testcase-service
if errorlevel 1 (
    echo testcase-service build failed!
    pause
    exit /b 1
)
echo.

echo --- Building defect-service ---
docker compose build defect-service
if errorlevel 1 (
    echo defect-service build failed!
    pause
    exit /b 1
)
echo.

echo --- Building requirement-service ---
docker compose build requirement-service
if errorlevel 1 (
    echo requirement-service build failed!
    pause
    exit /b 1
)
echo.

echo --- Building dashboard-service ---
docker compose build dashboard-service
if errorlevel 1 (
    echo dashboard-service build failed!
    pause
    exit /b 1
)
echo.

echo --- Building strategy-service ---
docker compose build strategy-service
if errorlevel 1 (
    echo strategy-service build failed!
    pause
    exit /b 1
)
echo.

echo --- Building ai-service ---
docker compose build ai-service
if errorlevel 1 (
    echo ai-service build failed!
    pause
    exit /b 1
)
echo.

echo --- Building frontend ---
docker compose build frontend
if errorlevel 1 (
    echo frontend build failed!
    pause
    exit /b 1
)
echo.

echo [Step 4] Start all services...
docker compose up -d
if errorlevel 1 (
    echo Services start failed!
    pause
    exit /b 1
)
echo.

echo ========================================
echo Deployment Complete!
echo ========================================
echo.
echo Access URLs:
echo   - Frontend: http://localhost:80
echo   - Auth Service: http://localhost:8001
echo   - User Service: http://localhost:8002
echo   - Project Service: http://localhost:8003
echo   - Testcase Service: http://localhost:8004
echo   - Defect Service: http://localhost:8005
echo   - Requirement Service: http://localhost:8006
echo   - Dashboard Service: http://localhost:8007
echo   - Strategy Service: http://localhost:8008
echo   - AI Service: http://localhost:8009
echo.
echo View logs: docker compose logs -f
echo Stop services: docker compose down
echo.
pause
