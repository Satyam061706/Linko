@echo off
setlocal
color 0B
echo.
echo ============================================================
echo   Starting Linko - Contact Management Application
echo ============================================================
echo.

:: Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] Java 17 or higher is required but not found in your PATH.
    echo Please install JDK 17 and try again.
    pause
    exit /b
)

:: Check if JAR exists
if not exist "Linko.jar" (
    echo [ERROR] Linko.jar not found in current folder.
    echo Rebuilding the project...
    call mvnw.cmd clean package -DskipTests
    copy target\Linko.jar .\Linko.jar
)

echo [INFO] Launching the application...
start /b java -jar Linko.jar > app.log 2>&1

echo [INFO] Waiting for the application to initialize...
echo.

:: Simple delay to let the app start
timeout /t 10 /nobreak > nul

echo [SUCCESS] Opening Linko in your default browser: http://localhost:8080
start http://localhost:8080

echo.
echo ============================================================
echo    Linko is now running! 
echo    Keep this window open to maintain the server.
echo    Press any key to STOP the application and exit.
echo ============================================================
echo.

pause

:: Find and kill the process running Linko
for /f "tokens=5" %%a in ('netstat -aon ^| findstr :8080') do taskkill /f /pid %%a >nul 2>&1

echo [INFO] Application stopped.
exit /b
