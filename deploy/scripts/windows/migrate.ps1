# AI测试管理平台 - 目录迁移脚本 (UTF-8编码)
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8

Write-Host "================================================"
Write-Host "AI测试管理平台 - 目录迁移脚本"
Write-Host "================================================"

Write-Host "[1/4] 创建新目录结构..."

$directories = @(
    "services",
    "ai-infra",
    "deploy\nginx\conf.d",
    "deploy\nginx\ssl",
    "deploy\scripts\windows",
    "deploy\scripts\linux",
    "deploy\scripts\powershell",
    "data\mysql",
    "data\redis",
    "data\rabbitmq",
    "data\postgres",
    "data\ollama",
    "data\open-webui",
    "data\anythingllm",
    "data\ragflow",
    "data\n8n",
    "data\chroma",
    "data\milvus",
    "data\qdrant",
    "logs\backend",
    "logs\frontend",
    "logs\ai-service",
    "logs\gateway",
    "logs\mysql",
    "logs\redis",
    "logs\rabbitmq",
    "logs\ollama",
    "logs\open-webui",
    "logs\anythingllm",
    "logs\ragflow",
    "logs\n8n",
    "logs\system",
    "monitoring\prometheus",
    "monitoring\grafana",
    "monitoring\loki",
    "monitoring\alertmanager",
    "docs\api",
    "docs\architecture",
    "docs\design",
    "docs\requirements",
    "docs\development",
    "docs\deployment",
    "docs\testing",
    "docs\ai\prompts",
    "docs\ai\rag",
    "docs\ai\agents",
    "docs\ai\workflows",
    "docs\reports",
    "assets\images",
    "assets\diagrams",
    "assets\videos",
    "assets\templates",
    "temp",
    "workspace\uploads",
    "workspace\generated",
    "workspace\embeddings",
    "workspace\exports",
    "tests\unit",
    "tests\integration",
    "tests\performance",
    "tests\security",
    "tests\ai-eval"
)

foreach ($dir in $directories) {
    $path = Join-Path $PWD $dir
    if (!(Test-Path $path)) {
        New-Item -ItemType Directory -Path $path -Force | Out-Null
    }
}

Write-Host "目录结构创建完成"

Write-Host "[2/4] 迁移后端服务..."

$backendMappings = @(
    @{ Source = "backend"; Target = "apps\backend" }
)

foreach ($mapping in $backendMappings) {
    $sourcePath = Join-Path $PWD $mapping.Source
    $targetPath = Join-Path $PWD $mapping.Target
    if (Test-Path $sourcePath) {
        Copy-Item -Path $sourcePath -Destination $targetPath -Recurse -Force
        Write-Host "迁移 $($mapping.Source) -> $($mapping.Target)"
    }
}

Write-Host "后端服务迁移完成"

Write-Host "[3/4] 迁移前端应用..."

$frontendMappings = @(
    @{ Source = "frontend"; Target = "apps\frontend" },
    @{ Source = "ai-service"; Target = "apps\ai-service" }
)

foreach ($mapping in $frontendMappings) {
    $sourcePath = Join-Path $PWD $mapping.Source
    $targetPath = Join-Path $PWD $mapping.Target
    if (Test-Path $sourcePath) {
        Copy-Item -Path $sourcePath -Destination $targetPath -Recurse -Force
        Write-Host "迁移 $($mapping.Source) -> $($mapping.Target)"
    }
}

Write-Host "前端应用迁移完成"

Write-Host "[4/4] 迁移Docker配置..."

$dockerMappings = @(
    @{ Source = "docker"; Target = "services" }
)

foreach ($mapping in $dockerMappings) {
    $sourcePath = Join-Path $PWD $mapping.Source
    $targetPath = Join-Path $PWD $mapping.Target
    if (Test-Path $sourcePath) {
        Copy-Item -Path $sourcePath -Destination $targetPath -Recurse -Force
        Write-Host "迁移 $($mapping.Source) -> $($mapping.Target)"
    }
}

Write-Host "Docker配置迁移完成"

Write-Host "================================================"
Write-Host "迁移完成"
Write-Host "================================================"