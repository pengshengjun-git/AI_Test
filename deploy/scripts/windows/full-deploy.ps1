param(
    [switch]$BuildOnly,
    [switch]$DeployOnly
)

$ErrorActionPreference = "Continue"

Write-Host "==========================================" -ForegroundColor Cyan
Write-Host "AI Test Platform - Full Deployment Script" -ForegroundColor Cyan
Write-Host "==========================================" -ForegroundColor Cyan

$services = @(
    @{name="auth-service"; display="Auth Service"},
    @{name="user-service"; display="User Service"},
    @{name="project-service"; display="Project Service"},
    @{name="testcase-service"; display="Testcase Service"},
    @{name="defect-service"; display="Defect Service"},
    @{name="requirement-service"; display="Requirement Service"},
    @{name="dashboard-service"; display="Dashboard Service"},
    @{name="strategy-service"; display="Strategy Service"},
    @{name="ai-service"; display="AI Service"},
    @{name="mock-service"; display="Mock Service"},
    @{name="frontend"; display="Frontend"}
)

if (-not $DeployOnly) {
    Write-Host ""
    Write-Host "[BUILD] Starting to build all service images..." -ForegroundColor Yellow

    foreach ($service in $services) {
        Write-Host ""
        Write-Host "Building: $($service.display)..." -ForegroundColor Cyan
        
        try {
            $result = docker build -t "ai-test-$($service.name)" -f "services/$($service.name)/Dockerfile" . 2>&1
            if ($LASTEXITCODE -eq 0) {
                Write-Host "$($service.display) built successfully!" -ForegroundColor Green
            } else {
                Write-Host "$($service.display) build failed!" -ForegroundColor Red
                $result | ForEach-Object { Write-Host "  $_" -ForegroundColor Red }
            }
        } catch {
            Write-Host "$($service.display) build error: $_" -ForegroundColor Red
        }
    }

    Write-Host ""
    Write-Host "[BUILD] Build completed!" -ForegroundColor Green
}

if (-not $BuildOnly) {
    Write-Host ""
    Write-Host "[DEPLOY] Starting deployment..." -ForegroundColor Yellow

    Write-Host ""
    Write-Host "Stopping and removing old containers..." -ForegroundColor Cyan
    docker stop $(docker ps -aq --filter "name=ai-test-*") 2>$null | Out-Null
    docker rm $(docker ps -aq --filter "name=ai-test-*") 2>$null | Out-Null

    Write-Host ""
    Write-Host "Starting Docker Compose..." -ForegroundColor Cyan
    docker compose -f deploy/docker-compose.yml up -d

    if ($LASTEXITCODE -eq 0) {
        Write-Host ""
        Write-Host "[DEPLOY] Deployment successful!" -ForegroundColor Green
        Write-Host ""
        Write-Host "Running containers:" -ForegroundColor Cyan
        docker ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"
    } else {
        Write-Host "[DEPLOY] Deployment failed!" -ForegroundColor Red
    }
}

Write-Host ""
Write-Host "==========================================" -ForegroundColor Cyan