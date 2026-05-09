@echo off
TITLE Linko - Smart Cloud Contact Manager
SETLOCAL EnableDelayedExpansion

:: --- CONFIGURATION ---
SET "APP_NAME=Linko"
SET "PORT=8080"
SET "JAVA_MIN_VERSION=17"

:: --- COLORS ---
SET "ESC=[2"
SET "GREEN=%ESC%92m"
SET "CYAN=%ESC%96m"
SET "YELLOW=%ESC%93m"
SET "RED=%ESC%91m"
SET "RESET=%ESC%0m"

cls
echo %CYAN%======================================================================%RESET%
echo %GREEN%          🚀  %APP_NAME% - Smart Cloud Contact Manager%RESET%
echo %CYAN%======================================================================%RESET%
echo.

:: 1. Check for Java Installation
echo [%CYAN%INFO%RESET%] Checking for Java installation...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo %RED%[ERROR] Java is not installed or not in your PATH.%RESET%
    echo Please install Java %JAVA_MIN_VERSION% or higher to run this project.
    pause
    exit /b 1
)

:: 2. Check Java Version
for /f "tokens=3" %%g in ('java -version 2^>^&1 ^| findstr /i "version"') do (
    set "JVER=%%g"
    set "JVER=!JVER:"=!"
)
echo [%CYAN%INFO%RESET%] Detected Java version: !JVER!

:: 3. Run the Application
echo [%CYAN%INFO%RESET%] Starting the application using Maven Wrapper...
echo [%YELLOW%NOTE%RESET%] This may take a moment to compile and start on the first run.
echo.

:: Check if mvnw.cmd exists
if not exist "mvnw.cmd" (
    echo %RED%[ERROR] mvnw.cmd not found in the current directory.%RESET%
    echo Make sure you are running this from the project root.
    pause
    exit /b 1
)

:: Run spring-boot:run
echo [%CYAN%INFO%RESET%] Opening Chrome at http://localhost:%PORT% in 10 seconds...
start /b cmd /c "timeout /t 10 >nul && start chrome http://localhost:%PORT%"
call mvnw.cmd spring-boot:run

if %errorlevel% neq 0 (
    echo.
    echo %RED%[ERROR] Application failed to start.%RESET%
    echo Please check the logs above for details.
    pause
)

echo.
echo %GREEN%[SUCCESS] Application stopped.%RESET%
pause
