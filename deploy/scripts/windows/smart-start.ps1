param(
    [switch]$BackendOnly
)

$ErrorActionPreference = "Continue"

Write-Host "=========================================="
Write-Host "AI Test Platform - Smart Start Script"
Write-Host "=========================================="

function Get-RunningDockerServices {
    $running = @{}
    docker ps --format "{{.Names}}" 2>$null | ForEach-Object { $running[$_] = $true }
    return $running
}

function Get-PortUsage {
    $usage = @{}
    netstat -ano | Select-String "LISTENING" | ForEach-Object {
        if ($_ -match ":(\d+)\s+.*LISTENING\s+(\d+)") {
            $usage[$matches[1]] = $matches[2]
        }
    }
    return $usage
}

$runningServices = Get-RunningDockerServices
$portUsage = Get-PortUsage

Write-Host ""
Write-Host "[Docker Running Services]"
$runningDocker = docker ps --format "{{.Names}}: {{.Ports}}" 2>$null
if ($runningDocker) {
    $runningDocker | ForEach-Object { Write-Host "  $_" }
} else {
    Write-Host "  None"
}

Write-Host ""
Write-Host "[Port Status]"
$ports = @{
    "mysql" = 3306
    "redis" = 6379
    "rabbitmq" = 5672
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

foreach ($service in $ports.Keys) {
    $port = $ports[$service]
    if ($portUsage.ContainsKey([string]$port)) {
        Write-Host "  $service : $port (IN USE)" -ForegroundColor Yellow
    } else {
        Write-Host "  $service : $port (OK)" -ForegroundColor Green
    }
}

$needInfra = -not ($runningServices.ContainsKey("mysql") -and $runningServices.ContainsKey("redis"))
$needBackend = $BackendOnly -or (-not ($runningServices.ContainsKey("ai-test-auth-service")))

Write-Host ""
Write-Host "[Decision]"
if ($needInfra) {
    Write-Host "  - Need to start: MySQL, Redis, RabbitMQ"
}
if ($needBackend) {
    Write-Host "  - Need to start: Backend microservices"
}

if (-not $needInfra -and -not $needBackend) {
    Write-Host "  All services already running, nothing to do"
    Write-Host ""
    Write-Host "=========================================="
    exit 0
}

if ($needInfra -and $needBackend) {
    $composeFile = "deploy/docker-compose.yml"
    Write-Host "  Using: $composeFile (full)"
} elseif ($needBackend) {
    $composeFile = "deploy/docker-compose.backend-only.yml"
    Write-Host "  Using: $composeFile (backend only)"
} else {
    $composeFile = "deploy/docker-compose.infra-only.yml"
    Write-Host "  Using: $composeFile (infra only)"
}

Write-Host ""
Write-Host "[Starting...]"
docker compose -f $composeFile up -d

if ($LASTEXITCODE -eq 0) {
    Write-Host "Done!" -ForegroundColor Green
} else {
    Write-Host "Failed! Check Docker Desktop status" -ForegroundColor Red
}

Write-Host ""
Write-Host "=========================================="