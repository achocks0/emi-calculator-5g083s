setlocal

set "SCRIPT_DIR=%~dp0"
set "APP_NAME=Compound Interest Calculator"
set "JAR_FILE=%SCRIPT_DIR%..\target\compound-interest-calculator.jar"
set "JAVA_OPTS=-Xms256m -Xmx512m"

call :main
exit /b %ERRORLEVEL%

:main
echo ===================================================================
echo                  %APP_NAME%
echo ===================================================================

REM Check if Java is installed
call :check_java
if %ERRORLEVEL% neq 0 exit /b %ERRORLEVEL%

REM Check if JAR file exists
call :check_jar
if %ERRORLEVEL% neq 0 exit /b %ERRORLEVEL%

REM Run the application
call :run_application
exit /b %ERRORLEVEL%

:check_java
echo Checking Java installation...
where java >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo ERROR: Java not found in PATH. Please install Java 11 or later.
    exit /b 1
)

REM Check Java version (must be at least 11)
for /f tokens^=2-5^ delims^=.-_^" %%j in ('java -version 2^>^&1 ^| findstr /i "version"') do (
    set "JAVA_VERSION=%%j"
)
if not defined JAVA_VERSION (
    echo ERROR: Unable to determine Java version.
    exit /b 1
)
if %JAVA_VERSION% LSS 11 (
    echo ERROR: Java version must be 11 or higher. Current version: %JAVA_VERSION%
    exit /b 1
)
echo Java check passed. Using Java version: %JAVA_VERSION%
exit /b 0

:check_jar
echo Checking application JAR file...
if not exist "%JAR_FILE%" (
    echo ERROR: Application JAR file not found at:
    echo %JAR_FILE%
    echo Please build the application first using Maven:
    echo mvn clean package
    exit /b 1
)
echo Application JAR file found.
exit /b 0

:run_application
echo Running %APP_NAME%...
java %JAVA_OPTS% -jar "%JAR_FILE%"
if %ERRORLEVEL% neq 0 (
    echo ERROR: Application exited with error code: %ERRORLEVEL%
)
exit /b %ERRORLEVEL%