@echo off
chcp 65001 >nul
powershell -NoExit -Command "[Console]::OutputEncoding = [System.Text.Encoding]::UTF8; Set-Location '%~dp0..\..'; & '%~dp0migrate.ps1'"