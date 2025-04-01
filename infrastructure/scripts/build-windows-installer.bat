@echo off
setlocal enabledelayedexpansion

rem ============================================================================
rem Build Windows Installer Script for Compound Interest Calculator
rem
rem This script builds a native Windows MSI installer for the 
rem Compound Interest Calculator application using JPackage.
rem
rem Prerequisites:
rem - Java JDK 14 or later with JPackage
rem - WiX Toolset (v3.11.2 or later)
rem - Property files (common.properties and windows.properties)
rem ============================================================================

rem Define script constants and variables
set SCRIPT_DIR=%~dp0
set APP_NAME=Compound Interest Calculator
set APP_VERSION=1.0.0
set COMMON_PROPS=%SCRIPT_DIR%..\jpackage\common.properties
set WINDOWS_PROPS=%SCRIPT_DIR%..\jpackage\windows.properties
set OUTPUT_DIR=%SCRIPT_DIR%..\installer\windows
set LOG_DIR=%OUTPUT_DIR%\logs
set BUILD_LOG=%LOG_DIR%\build-windows.log
set JPACKAGE_PATH=
set JAVA_HOME=
set BUILD_SUCCESS=0

rem ============================================================================
rem Function to print a banner with script information
rem ============================================================================
:print_banner
echo ============================================================================
echo                  Windows Installer Build Script
echo                %APP_NAME% - Version %APP_VERSION%
echo                   %date% %time%
echo ============================================================================
echo.
goto :eof

rem ============================================================================
rem Function to check prerequisites
rem ============================================================================
:check_prerequisites
call :log_message "Checking prerequisites..." INFO

rem Check Java installation
if "%JAVA_HOME%"=="" (
    call :log_message "JAVA_HOME environment variable is not set." ERROR
    call :log_message "Please install Java JDK 14 or later and set JAVA_HOME." ERROR
    exit /b 1
)

if not exist "%JAVA_HOME%\bin\java.exe" (
    call :log_message "Java executable not found at %JAVA_HOME%\bin\java.exe" ERROR
    call :log_message "Please check your Java installation." ERROR
    exit /b 1
)

rem Check JPackage availability
for /f "tokens=*" %%i in ('where jpackage 2^>nul') do (
    set JPACKAGE_PATH=%%i
)

if "%JPACKAGE_PATH%"=="" (
    call :log_message "JPackage tool not found in PATH." ERROR
    call :log_message "Please ensure you have JDK 14 or later installed with jpackage available." ERROR
    exit /b 1
)

rem Check JPackage version
for /f "tokens=*" %%i in ('jpackage --version 2^>^&1') do (
    set JPACKAGE_VERSION=%%i
)
call :log_message "Found JPackage version: %JPACKAGE_VERSION%" INFO

rem Check WiX Toolset installation
where candle >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    call :log_message "WiX Toolset not found in PATH." ERROR
    call :log_message "Please install WiX Toolset v3.11.2 or later." ERROR
    exit /b 1
)

rem Check if required property files exist
if not exist "%COMMON_PROPS%" (
    call :log_message "Common properties file not found at %COMMON_PROPS%" ERROR
    exit /b 1
)

if not exist "%WINDOWS_PROPS%" (
    call :log_message "Windows properties file not found at %WINDOWS_PROPS%" ERROR
    exit /b 1
)

call :log_message "All prerequisites checked successfully." INFO
exit /b 0

rem ============================================================================
rem Function to load properties from property files
rem ============================================================================
:load_properties
call :log_message "Loading properties..." INFO

rem Check if property files exist
if not exist "%COMMON_PROPS%" (
    call :log_message "Common properties file not found at %COMMON_PROPS%" ERROR
    exit /b 1
)

if not exist "%WINDOWS_PROPS%" (
    call :log_message "Windows properties file not found at %WINDOWS_PROPS%" ERROR
    exit /b 1
)

rem Create a temporary file for processed properties
set TEMP_PROPS=%TEMP%\combined_props.txt
type nul > "%TEMP_PROPS%"

rem Process and combine both property files
call :process_properties "%COMMON_PROPS%" "%TEMP_PROPS%"
call :process_properties "%WINDOWS_PROPS%" "%TEMP_PROPS%"

rem Load combined properties
for /f "tokens=1,* delims==" %%a in (%TEMP_PROPS%) do (
    set "%%a=%%b"
)

rem Clean up temporary file
del "%TEMP_PROPS%" 2>nul

rem Verify essential properties are loaded
call :verify_property "app.name" "Application name"
if %ERRORLEVEL% NEQ 0 exit /b 1

call :verify_property "app.version" "Application version"
if %ERRORLEVEL% NEQ 0 exit /b 1

call :verify_property "app.vendor" "Application vendor"
if %ERRORLEVEL% NEQ 0 exit /b 1

call :verify_property "app.main.class" "Main class"
if %ERRORLEVEL% NEQ 0 exit /b 1

call :verify_property "app.main.jar" "Main JAR file"
if %ERRORLEVEL% NEQ 0 exit /b 1

call :verify_property "app.input.dir" "Input directory"
if %ERRORLEVEL% NEQ 0 exit /b 1

call :verify_property "app.output.dir" "Output directory"
if %ERRORLEVEL% NEQ 0 exit /b 1

call :verify_property "win.upgrade.uuid" "Windows upgrade UUID"
if %ERRORLEVEL% NEQ 0 exit /b 1

call :log_message "Properties loaded successfully." INFO
call :log_message "Application Name: %app.name%" INFO
call :log_message "Application Version: %app.version%" INFO
call :log_message "Application Vendor: %app.vendor%" INFO
exit /b 0

rem ============================================================================
rem Function to process a properties file and append to combined file
rem ============================================================================
:process_properties
set PROP_FILE=%~1
set OUTPUT_FILE=%~2

rem Process each line, skipping comments and empty lines
for /f "usebackq tokens=*" %%a in ("%PROP_FILE%") do (
    set "line=%%a"
    
    rem Skip comment lines starting with # and empty lines
    echo !line! | findstr /r "^#" >nul
    if !errorlevel! neq 0 (
        echo !line! | findstr /r "^$" >nul
        if !errorlevel! neq 0 (
            echo !line!>>"%OUTPUT_FILE%"
        )
    )
)
goto :eof

rem ============================================================================
rem Function to verify if a property is set
rem ============================================================================
:verify_property
set PROP_NAME=%~1
set PROP_DESC=%~2

call set PROP_VALUE=%%%PROP_NAME%%%

if not defined PROP_VALUE (
    call :log_message "Required property '%PROP_NAME%' (%PROP_DESC%) not defined in properties files." ERROR
    exit /b 1
)

call :log_message "Property '%PROP_NAME%' = '%PROP_VALUE%'" DEBUG
exit /b 0

rem ============================================================================
rem Function to set up the build environment
rem ============================================================================
:setup_environment
call :log_message "Setting up build environment..." INFO

rem Create output directory if it doesn't exist
if not exist "%OUTPUT_DIR%" (
    mkdir "%OUTPUT_DIR%"
    call :log_message "Created output directory: %OUTPUT_DIR%" INFO
)

rem Create logs directory if it doesn't exist
if not exist "%LOG_DIR%" (
    mkdir "%LOG_DIR%"
    call :log_message "Created logs directory: %LOG_DIR%" INFO
)

rem Initialize build log
echo ============================================================================ > "%BUILD_LOG%"
echo Windows Installer Build Log - %date% %time% >> "%BUILD_LOG%"
echo ============================================================================ >> "%BUILD_LOG%"
echo. >> "%BUILD_LOG%"

rem Log environment information
call :log_message "Build environment:" INFO
call :log_message "  Windows version: %OS%" INFO
call :log_message "  Java home: %JAVA_HOME%" INFO
for /f "tokens=*" %%i in ('"%JAVA_HOME%\bin\java" -version 2^>^&1') do (
    call :log_message "  %%i" INFO
)
call :log_message "  JPackage path: %JPACKAGE_PATH%" INFO
call :log_message "  JPackage version: %JPACKAGE_VERSION%" INFO

call :log_message "Build environment set up successfully." INFO
exit /b 0

rem ============================================================================
rem Function to prepare resources for the installer
rem ============================================================================
:prepare_resources
call :log_message "Preparing resources for installer build..." INFO

rem Define temp directory for build
set BUILD_TEMP=%OUTPUT_DIR%\temp
if not exist "%BUILD_TEMP%" (
    mkdir "%BUILD_TEMP%"
    call :log_message "Created temporary build directory: %BUILD_TEMP%" INFO
)

rem Clean previous build artifacts if they exist
if exist "%BUILD_TEMP%\input" (
    rmdir /s /q "%BUILD_TEMP%\input"
    call :log_message "Cleaned previous input directory" INFO
)
mkdir "%BUILD_TEMP%\input"
call :log_message "Created input directory: %BUILD_TEMP%\input" INFO

rem Resolve paths that might be relative
call :resolve_path "app.input.dir"
call :resolve_path "win.icon.path"
call :resolve_path "win.resource.dir"
call :resolve_path "win.wix.template"

rem Copy application JAR to input directory
call :log_message "Copying application JAR..." INFO
set JAR_PATH=!app.input.dir!\!app.main.jar!
if not exist "!JAR_PATH!" (
    call :log_message "Application JAR not found at !JAR_PATH!" ERROR
    exit /b 1
)
copy "!JAR_PATH!" "%BUILD_TEMP%\input\" > nul
call :log_message "Copied application JAR to %BUILD_TEMP%\input\!app.main.jar!" INFO

rem Copy Windows icon if specified
if defined win.icon.path (
    call :log_message "Copying application icon..." INFO
    if not exist "!win.icon.path!" (
        call :log_message "WARNING: Icon file not found at !win.icon.path!" WARN
    ) else (
        copy "!win.icon.path!" "%BUILD_TEMP%\input\" > nul
        for %%i in ("!win.icon.path!") do set ICON_FILENAME=%%~nxi
        call :log_message "Copied icon to %BUILD_TEMP%\input\!ICON_FILENAME!" INFO
    )
)

rem Copy WiX template if specified
if defined win.wix.template (
    call :log_message "Copying WiX template..." INFO
    if not exist "!win.wix.template!" (
        call :log_message "WARNING: WiX template file not found at !win.wix.template!" WARN
    ) else (
        copy "!win.wix.template!" "%BUILD_TEMP%\" > nul
        for %%i in ("!win.wix.template!") do set TEMPLATE_FILENAME=%%~nxi
        call :log_message "Copied WiX template to %BUILD_TEMP%\!TEMPLATE_FILENAME!" INFO
    )
)

rem Copy any additional resources
if defined win.resource.dir (
    call :log_message "Copying additional resources..." INFO
    if exist "!win.resource.dir!" (
        xcopy "!win.resource.dir!\*" "%BUILD_TEMP%\input\" /E /I /Y > nul
        call :log_message "Copied resources from !win.resource.dir! to %BUILD_TEMP%\input\" INFO
    ) else (
        call :log_message "WARNING: Resource directory not found at !win.resource.dir!" WARN
    )
)

call :log_message "Resources prepared successfully." INFO
exit /b 0

rem ============================================================================
rem Function to resolve a path that might be relative
rem ============================================================================
:resolve_path
set PATH_PROP=%~1
if not defined %PATH_PROP% goto :eof

call set PATH_VALUE=%%%PATH_PROP%%%

rem Check if path is relative (doesn't start with drive letter or UNC path)
echo !PATH_VALUE! | findstr /r "^[A-Za-z]:" > nul
if %ERRORLEVEL% NEQ 0 (
    echo !PATH_VALUE! | findstr /r "^[\\][\\]" > nul
    if %ERRORLEVEL% NEQ 0 (
        rem It's a relative path, make it absolute based on script dir
        set %PATH_PROP%=%SCRIPT_DIR%!PATH_VALUE!
        call :log_message "Resolved relative path %PATH_PROP% to %SCRIPT_DIR%!PATH_VALUE!" DEBUG
    )
)
goto :eof

rem ============================================================================
rem Function to build the Windows installer
rem ============================================================================
:build_installer
call :log_message "Building Windows installer..." INFO

rem Construct JPackage command
set JPACKAGE_CMD=jpackage --type msi

rem Add application metadata
set JPACKAGE_CMD=%JPACKAGE_CMD% --name "!app.name!"
set JPACKAGE_CMD=%JPACKAGE_CMD% --app-version "!app.version!"
set JPACKAGE_CMD=%JPACKAGE_CMD% --vendor "!app.vendor!"

if defined app.description (
    set JPACKAGE_CMD=%JPACKAGE_CMD% --description "!app.description!"
)

if defined app.copyright (
    set JPACKAGE_CMD=%JPACKAGE_CMD% --copyright "!app.copyright!"
)

rem Add Windows-specific parameters
if defined win.menu.group (
    set JPACKAGE_CMD=%JPACKAGE_CMD% --win-menu --win-menu-group "!win.menu.group!"
)

if defined win.shortcut.desktop (
    if "!win.shortcut.desktop!"=="true" (
        set JPACKAGE_CMD=%JPACKAGE_CMD% --win-shortcut
    )
)

if defined win.dir.chooser (
    if "!win.dir.chooser!"=="true" (
        set JPACKAGE_CMD=%JPACKAGE_CMD% --win-dir-chooser
    )
)

if defined win.per.user.install (
    if "!win.per.user.install!"=="true" (
        set JPACKAGE_CMD=%JPACKAGE_CMD% --win-per-user-install
    )
)

if defined win.upgrade.uuid (
    set JPACKAGE_CMD=%JPACKAGE_CMD% --win-upgrade-uuid "!win.upgrade.uuid!"
)

rem Add icon if it exists in input directory
for %%i in ("%BUILD_TEMP%\input\*.ico") do (
    set JPACKAGE_CMD=%JPACKAGE_CMD% --icon "%%i"
    call :log_message "Using icon: %%i" INFO
)

rem Add input and output directories
set JPACKAGE_CMD=%JPACKAGE_CMD% --input "%BUILD_TEMP%\input"
set JPACKAGE_CMD=%JPACKAGE_CMD% --dest "%OUTPUT_DIR%"

rem Add main jar and main class
set JPACKAGE_CMD=%JPACKAGE_CMD% --main-jar "!app.main.jar!"
set JPACKAGE_CMD=%JPACKAGE_CMD% --main-class "!app.main.class!"

rem Add resource directory if WiX template exists
for %%i in ("%BUILD_TEMP%\*.xml") do (
    set JPACKAGE_CMD=%JPACKAGE_CMD% --resource-dir "%BUILD_TEMP%"
    call :log_message "Using resource directory: %BUILD_TEMP% for WiX template" INFO
    goto :wix_template_found
)
:wix_template_found

rem Add Java runtime options if specified
if defined app.java.options (
    set JPACKAGE_CMD=%JPACKAGE_CMD% --java-options "!app.java.options!"
)

rem Add verbose output if specified
if defined app.verbose (
    if "!app.verbose!"=="true" (
        set JPACKAGE_CMD=%JPACKAGE_CMD% --verbose
    )
)

rem Execute JPackage command
call :log_message "Executing: %JPACKAGE_CMD%" INFO

%JPACKAGE_CMD% >> "%BUILD_LOG%" 2>&1
set RESULT=%ERRORLEVEL%

if %RESULT% NEQ 0 (
    call :log_message "JPackage execution failed with exit code %RESULT%." ERROR
    call :log_message "See log file for details: %BUILD_LOG%" ERROR
    exit /b %RESULT%
)

call :log_message "Installer built successfully." INFO
exit /b 0

rem ============================================================================
rem Function to verify the created installer
rem ============================================================================
:verify_installer
call :log_message "Verifying installer..." INFO

rem Check if installer file was created
set MSI_FILE=%OUTPUT_DIR%\!app.name!-!app.version!.msi
if not exist "!MSI_FILE!" (
    rem Try with spaces replaced by underscores
    set APP_NAME_SAFE=!app.name: =_!
    set MSI_FILE=%OUTPUT_DIR%\!APP_NAME_SAFE!-!app.version!.msi
    
    if not exist "!MSI_FILE!" (
        call :log_message "Installer file not found at expected locations" ERROR
        call :log_message "Tried: %OUTPUT_DIR%\!app.name!-!app.version!.msi" ERROR
        call :log_message "Tried: %OUTPUT_DIR%\!APP_NAME_SAFE!-!app.version!.msi" ERROR
        
        rem Last resort - try to find any MSI file in the output directory
        for %%i in ("%OUTPUT_DIR%\*.msi") do (
            call :log_message "Found MSI file: %%i" INFO
            set MSI_FILE=%%i
            goto :msi_file_found
        )
        
        call :log_message "No MSI files found in output directory" ERROR
        exit /b 1
    )
)
:msi_file_found

rem Check file size (should be at least 1MB for a valid installer)
for %%i in ("!MSI_FILE!") do set FILE_SIZE=%%~zi
if !FILE_SIZE! LSS 1000000 (
    call :log_message "WARNING: Installer file size seems too small (!FILE_SIZE! bytes)." WARN
) else (
    rem Convert file size to MB for display
    set /a FILE_SIZE_MB=!FILE_SIZE! / 1048576
    call :log_message "Installer file size: !FILE_SIZE_MB! MB" INFO
)

call :log_message "Installer verified: !MSI_FILE!" INFO
exit /b 0

rem ============================================================================
rem Function to clean up temporary files
rem ============================================================================
:cleanup
call :log_message "Cleaning up temporary files..." INFO

rem Remove temporary build directory
if exist "%BUILD_TEMP%" (
    rmdir /s /q "%BUILD_TEMP%"
    call :log_message "Removed temporary build directory: %BUILD_TEMP%" INFO
)

call :log_message "Cleanup completed." INFO
exit /b 0

rem ============================================================================
rem Function to log a message
rem ============================================================================
:log_message
set MESSAGE=%~1
set LEVEL=%~2
if "%LEVEL%"=="" set LEVEL=INFO

echo [%date% %time%] [%LEVEL%] %MESSAGE%
echo [%date% %time%] [%LEVEL%] %MESSAGE% >> "%BUILD_LOG%"
goto :eof

rem ============================================================================
rem Main function
rem ============================================================================
:main
call :print_banner

rem Setup environment
call :setup_environment
if %ERRORLEVEL% NEQ 0 (
    call :log_message "Failed to set up environment. Exiting." ERROR
    exit /b 1
)

rem Check prerequisites
call :check_prerequisites
if %ERRORLEVEL% NEQ 0 (
    call :log_message "Prerequisites check failed. Exiting." ERROR
    exit /b 1
)

rem Load properties
call :load_properties
if %ERRORLEVEL% NEQ 0 (
    call :log_message "Failed to load properties. Exiting." ERROR
    exit /b 1
)

rem Prepare resources
call :prepare_resources
if %ERRORLEVEL% NEQ 0 (
    call :log_message "Failed to prepare resources. Exiting." ERROR
    exit /b 1
)

rem Build installer
call :build_installer
set BUILD_RESULT=%ERRORLEVEL%
if %BUILD_RESULT% NEQ 0 (
    call :log_message "Installer build failed with code %BUILD_RESULT%. Exiting." ERROR
    exit /b %BUILD_RESULT%
)

rem Verify installer
call :verify_installer
if %ERRORLEVEL% NEQ 0 (
    call :log_message "Installer verification failed. Exiting." ERROR
    exit /b 1
)

rem Clean up
call :cleanup

rem Print summary
echo.
call :log_message "============================================================================" INFO
call :log_message "                    Build Process Completed Successfully" INFO
call :log_message "============================================================================" INFO
for %%i in ("%OUTPUT_DIR%\*.msi") do (
    call :log_message "Installer: %%i" INFO
)
call :log_message "Log file: %BUILD_LOG%" INFO
call :log_message "============================================================================" INFO
echo.

set BUILD_SUCCESS=1
exit /b 0

rem Start execution from main
call :main
set FINAL_RESULT=%ERRORLEVEL%

if %BUILD_SUCCESS%==1 (
    exit /b 0
) else (
    exit /b %FINAL_RESULT%
)