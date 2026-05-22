# AI测试平台 - PowerShell状态检查脚本
# 使用方法: .\check-status.ps1

$ErrorActionPreference = "Stop"
$ProjectRoot = Split-Path -Parent $PSScriptRoot | Split-Path -Parent
$DockerDir = Join-Path $ProjectRoot "docker"

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "AI测试平台 - 服务状态检查" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# 检查Docker是否运行
Write-Host "[1] 检查Docker运行状态..." -ForegroundColor Yellow
try {
    docker info | Out-Null
    Write-Host "✅ Docker运行正常" -ForegroundColor Green
} catch {
    Write-Host "❌ Docker未运行，请先启动Docker Desktop" -ForegroundColor Red
    exit 1
}
Write-Host ""

# 切换到docker目录并检查容器状态
Write-Host "[2] 查看所有容器状态..." -ForegroundColor Yellow
Set-Location $DockerDir
docker compose ps
Write-Host ""

# 检查端口
Write-Host "[3] 检查关键端口..." -ForegroundColor Yellow
$ports = @{
    "auth-service" = 8001
    "dashboard-service" = 8007
    "strategy-service" = 8008
    "frontend" = 80
}

foreach ($service in $ports.Keys) {
    $port = $ports[$service]
    $connection = Test-NetConnection -ComputerName localhost -Port $port -WarningAction SilentlyContinue
    if ($connection.TcpTestSucceeded) {
        Write-Host "✅ $service ($port) 端口已监听" -ForegroundColor Green
    } else {
        Write-Host "❌ $service ($port) 端口未监听" -ForegroundColor Red
    }
}
Write-Host ""

# 测试API接口
Write-Host "[4] 测试API接口..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8007/api/v1/dashboard/stats" -Method Get -UseBasicParsing -TimeoutSec 5
    Write-Host "✅ 工作台服务响应正常" -ForegroundColor Green
} catch {
    Write-Host "❌ 工作台服务无响应" -ForegroundColor Red
}

try {
    $response = Invoke-WebRequest -Uri "http://localhost:8008/api/v1/strategy" -Method Get -UseBasicParsing -TimeoutSec 5
    Write-Host "✅ 策略服务响应正常" -ForegroundColor Green
} catch {
    Write-Host "❌ 策略服务无响应" -ForegroundColor Red
}
Write-Host ""

# 访问建议
Write-Host "[5] 访问建议..." -ForegroundColor Yellow
Write-Host "前端界面: http://localhost" -ForegroundColor Cyan
Write-Host "工作台API: http://localhost:8007/api/v1/dashboard/stats" -ForegroundColor Cyan
Write-Host "策略API: http://localhost:8008/api/v1/strategy" -ForegroundColor Cyan
Write-Host ""

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "查看日志命令: cd docker; docker compose logs -f" -ForegroundColor White
Write-Host "停止服务命令: cd docker; docker compose down" -ForegroundColor White
Write-Host "========================================" -ForegroundColor Cyan
