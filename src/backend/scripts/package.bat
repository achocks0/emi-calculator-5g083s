@echo off
setlocal enabledelayedexpansion

REM -------------------------------------------------------------------------
REM Windows batch script for packaging the Compound Interest Calculator
REM application into MSI, EXE and runnable JAR formats.
REM 
REM This script handles the Maven build process and uses JPackage to create
REM native Windows installers and executable JAR files.
REM 
REM Prerequisites:
REM   - Java 11+ (for compilation)
REM   - Java 14+ (for jpackage)
REM   - Maven 3.8.6+
REM -------------------------------------------------------------------------

REM Set global variables
set "SCRIPT_DIR=%~dp0"
set "APP_NAME=Compound Interest Calculator"
set "APP_VERSION=1.0.0"
set "MAIN_CLASS=com.bank.calculator.CompoundInterestCalculatorApp"
set "TARGET_DIR=%SCRIPT_DIR%..\target"
set "JAR_FILE=%TARGET_DIR%\compound-interest-calculator-%APP_VERSION%-with-dependencies.jar"
set "DIST_DIR=%SCRIPT_DIR%..\dist"
set "ICON_PATH=%SCRIPT_DIR%..\src\main\resources\icons\calculator-icon.png"
set "TEMP_DIR=%SCRIPT_DIR%..\temp"

REM Main function that orchestrates the packaging process
call :main
exit /b %ERRORLEVEL%

:main
    echo.
    echo ======================================================================
    echo Packaging %APP_NAME% v%APP_VERSION%
    echo ======================================================================
    echo.

    REM Check for required tools
    call :check_prerequisites
    if %ERRORLEVEL% neq 0 (
        echo ERROR: Prerequisites check failed. Exiting.
        exit /b 1
    )

    REM Build the application using Maven
    call :build_jar
    if %ERRORLEVEL% neq 0 (
        echo ERROR: Maven build failed. Exiting.
        exit /b 1
    )

    REM Create distribution directory if it doesn't exist
    if not exist "%DIST_DIR%" (
        echo Creating distribution directory: %DIST_DIR%
        mkdir "%DIST_DIR%"
    )

    REM Create Windows MSI installer
    call :create_msi
    if %ERRORLEVEL% neq 0 (
        echo WARNING: MSI creation failed, continuing with other formats.
    )

    REM Create Windows EXE installer
    call :create_exe
    if %ERRORLEVEL% neq 0 (
        echo WARNING: EXE creation failed, continuing with other formats.
    )

    REM Create JAR with dependencies
    call :create_jar_with_dependencies
    if %ERRORLEVEL% neq 0 (
        echo WARNING: JAR packaging failed.
    )

    REM Print summary
    echo.
    echo ======================================================================
    echo Packaging Complete
    echo ======================================================================
    echo.
    echo The following packages have been created in: %DIST_DIR%
    if exist "%DIST_DIR%\%APP_NAME%-%APP_VERSION%.msi" echo - MSI Installer: %APP_NAME%-%APP_VERSION%.msi
    if exist "%DIST_DIR%\%APP_NAME%-%APP_VERSION%.exe" echo - EXE Installer: %APP_NAME%-%APP_VERSION%.exe
    if exist "%DIST_DIR%\%APP_NAME%-%APP_VERSION%.jar" echo - JAR Package: %APP_NAME%-%APP_VERSION%.jar
    echo.

    exit /b 0

:check_prerequisites
    echo Checking prerequisites...
    
    REM Check Java version
    java -version >nul 2>&1
    if %ERRORLEVEL% neq 0 (
        echo ERROR: Java not found. Please install Java 11 or higher.
        exit /b 1
    )
    
    REM Check if Java version is 11 or higher
    for /f tokens^=2-5^ delims^=.-_+^" %%j in ('java -version 2^>^&1') do (
        if "%%j" == "1" (
            if %%k LSS 11 (
                echo ERROR: Java 11 or higher is required. Current version: 1.%%k
                exit /b 1
            )
        )
    )
    
    REM Check Maven installation
    mvn -version >nul 2>&1
    if %ERRORLEVEL% neq 0 (
        echo ERROR: Maven not found. Please install Maven 3.8.6 or higher.
        exit /b 1
    )
    
    REM Check if JPackage is available (Java 14+ required)
    jpackage --version >nul 2>&1
    if %ERRORLEVEL% neq 0 (
        echo ERROR: jpackage tool not found. Make sure you have JDK 14 or higher installed.
        exit /b 1
    )
    
    echo All prerequisites satisfied.
    exit /b 0

:build_jar
    echo.
    echo Building application with Maven...
    echo.
    
    REM Change to the project root directory
    pushd "%SCRIPT_DIR%..\"
    
    REM Clean and package the application
    call mvn clean package
    if %ERRORLEVEL% neq 0 (
        echo ERROR: Maven build failed.
        popd
        exit /b 1
    )
    
    REM Check if JAR was created
    if not exist "%JAR_FILE%" (
        echo ERROR: JAR file not found at %JAR_FILE%
        popd
        exit /b 1
    )
    
    echo Maven build successful: %JAR_FILE%
    popd
    exit /b 0

:create_msi
    echo.
    echo Creating Windows MSI installer...
    echo.
    
    REM Create temporary directory for packaging
    if not exist "%TEMP_DIR%" mkdir "%TEMP_DIR%"
    
    REM Use jpackage to create MSI
    jpackage ^
        --type msi ^
        --input "%TARGET_DIR%" ^
        --dest "%DIST_DIR%" ^
        --name "%APP_NAME%" ^
        --app-version "%APP_VERSION%" ^
        --main-jar "%JAR_FILE:~0,-4%.jar" ^
        --main-class "%MAIN_CLASS%" ^
        --win-dir-chooser ^
        --win-menu ^
        --win-shortcut ^
        --icon "%ICON_PATH%" ^
        --vendor "Banking Division" ^
        --description "Calculator for compound interest and EMI" ^
        --copyright "Banking Division" ^
        --temp "%TEMP_DIR%"
    
    if %ERRORLEVEL% neq 0 (
        echo ERROR: Failed to create MSI package.
        exit /b 1
    )
    
    echo MSI package created successfully: %DIST_DIR%\%APP_NAME%-%APP_VERSION%.msi
    exit /b 0

:create_exe
    echo.
    echo Creating Windows EXE installer...
    echo.
    
    REM Create temporary directory for packaging
    if not exist "%TEMP_DIR%" mkdir "%TEMP_DIR%"
    
    REM Use jpackage to create EXE
    jpackage ^
        --type exe ^
        --input "%TARGET_DIR%" ^
        --dest "%DIST_DIR%" ^
        --name "%APP_NAME%" ^
        --app-version "%APP_VERSION%" ^
        --main-jar "%JAR_FILE:~0,-4%.jar" ^
        --main-class "%MAIN_CLASS%" ^
        --win-dir-chooser ^
        --win-menu ^
        --win-shortcut ^
        --icon "%ICON_PATH%" ^
        --vendor "Banking Division" ^
        --description "Calculator for compound interest and EMI" ^
        --copyright "Banking Division" ^
        --temp "%TEMP_DIR%"
    
    if %ERRORLEVEL% neq 0 (
        echo ERROR: Failed to create EXE package.
        exit /b 1
    )
    
    echo EXE package created successfully: %DIST_DIR%\%APP_NAME%-%APP_VERSION%.exe
    exit /b 0

:create_jar_with_dependencies
    echo.
    echo Creating runnable JAR package...
    echo.
    
    REM Create directory for JAR
    set "JAR_DIST_DIR=%DIST_DIR%\jar"
    if not exist "%JAR_DIST_DIR%" mkdir "%JAR_DIST_DIR%"
    
    REM Copy the built JAR to the distribution directory
    copy "%JAR_FILE%" "%DIST_DIR%\%APP_NAME%-%APP_VERSION%.jar" >nul
    if %ERRORLEVEL% neq 0 (
        echo ERROR: Failed to copy JAR file to distribution directory.
        exit /b 1
    )
    
    REM Create a batch script to run the JAR
    (
        echo @echo off
        echo java -jar "%APP_NAME%-%APP_VERSION%.jar"
    ) > "%DIST_DIR%\run-%APP_NAME%.bat"
    
    echo JAR package created successfully: %DIST_DIR%\%APP_NAME%-%APP_VERSION%.jar
    echo Run script created: %DIST_DIR%\run-%APP_NAME%.bat
    exit /b 0