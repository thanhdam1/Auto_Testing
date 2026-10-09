@echo off
setlocal

set "PATH=C:\Users\PC\Downloads\apache-maven-3.9.11-bin\apache-maven-3.9.11\bin;%PATH%"

echo ==========================================================
echo       E-COMMERCE TEST AUTOMATION TEST RUNNER (CMD)
echo ==========================================================
echo 1. Chay Smoke Tests (Kiem thu nhanh)
echo 2. Chay SauceDemo Regression Suite (Mac dinh)
echo 3. Chay Toan bo (SauceDemo + Automation Exercise)
echo 4. Chay Song song da luong (Parallel Execution)
echo ==========================================================
set /p choice="Nhap lua chon cua ban (1-4, mac dinh la 2): "

if "%choice%"=="1" (
    call mvn test "-DsuiteFile=testng-smoke.xml"
) else if "%choice%"=="3" (
    call mvn test "-DsuiteFile=testng-all.xml"
) else if "%choice%"=="4" (
    call mvn test "-DsuiteFile=testng-parallel.xml"
) else (
    call mvn test "-DsuiteFile=testng.xml"
)

echo.
echo ==========================================================
echo Kiem tra bao cao tai thu muc: reports\
echo ==========================================================
pause
