# AI测试管理平台 - 智能启动脚本
param(
    [switch]$BackendOnly,
    [switch]$Status
)

$ErrorActionPreference = "Continue"

function Write-Log {
    param([string]$Message, [string]$Level = "INFO")
    $timestamp = Get-Date -Format "yyyy-MM-dd HH:mm:ss"
    Write-Host "[$timestamp] [$Level] $Message"
}

function Get-RunningServices {
    $running = @{}
    docker ps --format "{{.Names}}" 2>$null | ForEach-Object { $running[$_] = $true }
    return $running
}

function Get-PortUsage {
    $usage = @{}
    $result = netstat -ano | Select-String "LISTENING"
    foreach ($line in $result) {
        if ($line -match ":(\d+)\s+.*LISTENING\s+(\d+)") {
            $port = $matches[1]
            $pid = $matches[2]
            $usage[$port] = $pid
        }
    }
    return $usage
}

Write-Log "=========================================="
Write-Log "AI测试管理平台 - 智能启动脚本"
Write-Log "=========================================="

# 获取运行中的服务和端口占用
$runningServices = Get-RunningServices
$portUsage = Get-PortUsage

# 定义服务端口映射
$servicePorts = @{
    "mysql" = 3306
    "redis" = 6379
    "rabbitmq" = 5672
    "openwebui" = 8080
    "auth-service" = 8001
    "user-service" = 8002
    "project-service" = 8003
    "testcase-service" = 8004
    "defect-service" = 8005
    "requirement-service" = 8006
    "dashboard-service" = 8007
    "strategy-service" = 8008
    "ai-service" = 8010
    "frontend" = 80
}

Write-Log ""
Write-Log "【当前运行中的Docker服务】"
$runningDocker = docker ps --format "{{.Names}}: {{.Ports}}" 2>$null
if ($runningDocker) {
    $runningDocker | ForEach-Object { Write-Log "  $_" }
} else {
    Write-Log "  无"
}

Write-Log ""
Write-Log "【端口占用情况】"
foreach ($service in $servicePorts.Keys) {
    $port = $servicePorts[$service]
    if ($portUsage.ContainsKey([string]$port)) {
        Write-Log "  $service : $port (PID: $($portUsage[[string]$port]))" -Level "WARN"
    } else {
        Write-Log "  $service : $port (可用)" -Level "INFO"
    }
}

# 判断启动方案
$needInfrastructure = -not ($runningServices.ContainsKey("mysql") -and $runningServices.ContainsKey("redis"))
$needBackend = $BackendOnly -or (-not ($runningServices.ContainsKey("ai-test-auth-service")))

Write-Log ""
Write-Log "【启动决策】"
if ($needInfrastructure) {
    Write-Log "  - 需要启动: MySQL, Redis, RabbitMQ (基础设施)"
}
if ($needBackend) {
    Write-Log "  - 需要启动: 后端微服务"
}

if (-not $needInfrastructure -and -not $needBackend) {
    Write-Log "  所有服务已在运行中，无需启动" -Level "INFO"
    Write-Log ""
    Write-Log "=========================================="
    exit 0
}

# 选择配置文件
if ($needInfrastructure -and $needBackend) {
    $composeFile = "deploy/docker-compose.yml"
    Write-Log "  使用配置文件: $composeFile (完整启动)"
} elseif ($needBackend) {
    $composeFile = "deploy/docker-compose.backend-only.yml"
    Write-Log "  使用配置文件: $composeFile (仅后端)"
} else {
    $composeFile = "deploy/docker-compose.infra-only.yml"
    Write-Log "  使用配置文件: $composeFile (仅基础设施)"
}

Write-Log ""
Write-Log "【启动命令】"
Write-Log "  docker compose -f $composeFile up -d"

Write-Log ""
Write-Log "正在执行启动..."
docker compose -f $composeFile up -d

if ($LASTEXITCODE -eq 0) {
    Write-Log "启动完成!" -Level "INFO"
} else {
    Write-Log "启动失败，请检查Docker Desktop状态" -Level "ERROR"
}

Write-Log ""
Write-Log "=========================================="