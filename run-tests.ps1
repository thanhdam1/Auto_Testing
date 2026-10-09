<#
.SYNOPSIS
    Script khởi chạy E-Commerce Automation Testing Suite
.DESCRIPTION
    Tự động thiết lập Maven PATH nếu chưa có, hỗ trợ chọn Suite, Browser và Headless mode.
.EXAMPLE
    .\run-tests.ps1 -Suite smoke
    .\run-tests.ps1 -Suite all -Browser edge
    .\run-tests.ps1 -Suite full -Headless $true
#>

param(
    [ValidateSet("smoke", "full", "all", "parallel")]
    [string]$Suite = "full",

    [ValidateSet("chrome", "edge", "firefox")]
    [string]$Browser = "chrome",

    [bool]$Headless = $false
)

# Ensure Maven is in PATH
$mavenBin = "C:\Users\PC\Downloads\apache-maven-3.9.11-bin\apache-maven-3.9.11\bin"
if ($env:Path -notlike "*$mavenBin*") {
    $env:Path = "$mavenBin;" + $env:Path
}

Write-Host "==========================================================" -ForegroundColor Cyan
Write-Host "      E-COMMERCE TEST AUTOMATION TEST RUNNER             " -ForegroundColor Cyan
Write-Host "==========================================================" -ForegroundColor Cyan
Write-Host "Suite:    $Suite" -ForegroundColor Yellow
Write-Host "Browser:  $Browser" -ForegroundColor Yellow
Write-Host "Headless: $Headless" -ForegroundColor Yellow
Write-Host "==========================================================" -ForegroundColor Cyan

$suiteFile = switch ($Suite) {
    "smoke"    { "testng-smoke.xml" }
    "all"      { "testng-all.xml" }
    "parallel" { "testng-parallel.xml" }
    default    { "testng.xml" }
}

mvn test "-DsuiteFile=$suiteFile" "-Dbrowser=$Browser" "-Dheadless=$Headless"

Write-Host ""
Write-Host "Kiểm tra báo cáo HTML mới nhất tại thư mục: reports\" -ForegroundColor Green
