$OutputEncoding = [Console]::OutputEncoding = [System.Text.Encoding]::UTF8

Write-Host "================================================"
Write-Host "整理文档到对应目录"
Write-Host "================================================"

$docs = @(
    @{ Source = "API.md"; Target = "docs/api/API文档.md" },
    @{ Source = "DESIGN.md"; Target = "docs/design/设计文档.md" },
    @{ Source = "DEVELOPMENT.md"; Target = "docs/development/开发文档.md" },
    @{ Source = "FIX_DOCUMENT.md"; Target = "docs/testing/修复文档.md" },
    @{ Source = "MIGRATION_PLAN_V2.md"; Target = "docs/deployment/迁移方案.md" },
    @{ Source = "PROJECT_SUMMARY.md"; Target = "docs/项目总结.md" },
    @{ Source = "AI 测试管理平台详细设计文档.docx"; Target = "docs/design/详细设计文档.docx" },
    @{ Source = "AI测试管理平台_补充设计文档.docx"; Target = "docs/design/补充设计文档.docx" },
    @{ Source = "AI测试系统架构图.png"; Target = "docs/architecture/架构图.png" },
    @{ Source = "AI测试系统架构图.png"; Target = "docs/architecture/架构图.png" },
    @{ Source = "功能自检清单.md"; Target = "docs/testing/功能自检清单.md" },
    @{ Source = "技术文档.md"; Target = "docs/architecture/技术文档.md" },
    @{ Source = "开发文档排期.md"; Target = "docs/development/开发文档排期.md" },
    @{ Source = "需求清单.md"; Target = "docs/requirements/需求清单.md" }
)

foreach ($doc in $docs) {
    if (Test-Path $doc.Source) {
        $targetDir = Split-Path $doc.Target -Parent
        if (!(Test-Path $targetDir)) {
            New-Item -ItemType Directory -Path $targetDir -Force | Out-Null
        }
        Copy-Item -Path $doc.Source -Destination $doc.Target -Force
        Write-Host "移动: $($doc.Source) -> $($doc.Target)"
        Remove-Item -Path $doc.Source -Force
    }
}

$scripts = @(
    @{ Source = "check-status.bat"; Target = "deploy/scripts/windows/check-status.bat" },
    @{ Source = "check-status.ps1"; Target = "deploy/scripts/powershell/check-status.ps1" },
    @{ Source = "check-status.sh"; Target = "deploy/scripts/linux/check-status.sh" },
    @{ Source = "start.bat"; Target = "deploy/scripts/windows/start.bat" },
    @{ Source = "start.sh"; Target = "deploy/scripts/linux/start.sh" }
)

foreach ($script in $scripts) {
    if (Test-Path $script.Source) {
        $targetDir = Split-Path $script.Target -Parent
        if (!(Test-Path $targetDir)) {
            New-Item -ItemType Directory -Path $targetDir -Force | Out-Null
        }
        Copy-Item -Path $script.Source -Destination $script.Target -Force
        Write-Host "移动: $($script.Source) -> $($script.Target)"
        Remove-Item -Path $script.Source -Force
    }
}

$logs = Get-ChildItem -Path . -Filter "hs_err_pid*.log"
foreach ($log in $logs) {
    Move-Item -Path $log.FullName -Destination "logs/system/$($log.Name)" -Force
    Write-Host "移动日志: $($log.Name) -> logs/system/"
}

$logs = Get-ChildItem -Path . -Filter "replay_pid*.log"
foreach ($log in $logs) {
    Move-Item -Path $log.FullName -Destination "logs/system/$($log.Name)" -Force
    Write-Host "移动日志: $($log.Name) -> logs/system/"
}

if (Test-Path "init-db.sql") {
    Move-Item -Path "init-db.sql" -Destination "apps/backend/database/init-db.sql" -Force
    Write-Host "移动: init-db.sql -> apps/backend/database/"
}

if (Test-Path "code.html") {
    Move-Item -Path "code.html" -Destination "temp/code.html" -Force
    Write-Host "移动: code.html -> temp/"
}

if (Test-Path "screen.png") {
    Move-Item -Path "screen.png" -Destination "assets/images/screen.png" -Force
    Write-Host "移动: screen.png -> assets/images/"
}

Remove-Item -Path "MIGRATION_PLAN.md" -Force -ErrorAction SilentlyContinue
Write-Host "删除重复: MIGRATION_PLAN.md"

Write-Host "================================================"
Write-Host "文档整理完成！"
Write-Host "================================================"