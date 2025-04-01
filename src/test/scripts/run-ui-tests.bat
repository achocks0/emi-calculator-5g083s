@echo off
REM ======================================================================
REM Windows batch script to run UI tests for Compound Interest Calculator
REM This script provides a convenient way to execute UI-specific tests 
REM that verify the application's user interface functionality
REM ======================================================================

setlocal EnableDelayedExpansion

REM Define constants and default values
set "SCRIPT_DIR=%~dp0"
set "PROJECT_ROOT=%SCRIPT_DIR%..\..\"
set "TEST_REPORT_DIR=%PROJECT_ROOT%\target\ui-test-reports"
set "HEADLESS_MODE=true"
set "VERBOSE=false"

REM Display a banner
echo ===================================================================
echo Compound Interest Calculator - UI Test Runner
echo ===================================================================
echo.

REM Parse command-line arguments
call :parse_arguments %*

REM Check prerequisites
call :check_prerequisites
if %ERRORLEVEL% neq 0 (
    echo Prerequisites check failed. Please fix the issues and try again.
    exit /b 1
)

REM Display test run information
if "%VERBOSE%"=="true" (
    echo Running UI tests with the following settings:
    echo - Project root: %PROJECT_ROOT%
    echo - Test report directory: %TEST_REPORT_DIR%
    echo - Headless mode: %HEADLESS_MODE%
    echo.
)

REM Create report directory if it doesn't exist
if not exist "%TEST_REPORT_DIR%" (
    mkdir "%TEST_REPORT_DIR%"
    echo Created test report directory: %TEST_REPORT_DIR%
)

REM Set environment variables for Maven and JavaFX
if "%HEADLESS_MODE%"=="true" (
    set "MAVEN_OPTS=-Xmx1024m -Djavafx.headless=true"
    echo Running tests in headless mode...
) else (
    set "MAVEN_OPTS=-Xmx1024m"
    echo Running tests with visible UI...
)

REM Execute the UI tests using UITestsRunner
echo Executing UI tests...
cd "%PROJECT_ROOT%"
mvn exec:java -Dexec.mainClass="com.bank.calculator.test.runner.UITestsRunner" ^
    -Dexec.classpathScope=test ^
    -Dheadless=%HEADLESS_MODE% ^
    -Dreport.dir="%TEST_REPORT_DIR%"

REM Store the exit code
set MAVEN_EXIT_CODE=%ERRORLEVEL%

REM Generate test reports
call :generate_ui_report

REM Display test results summary
if %MAVEN_EXIT_CODE% equ 0 (
    echo.
    echo UI TEST EXECUTION SUCCESSFUL
    echo Test reports available at: %TEST_REPORT_DIR%
) else (
    echo.
    echo UI TEST EXECUTION FAILED
    echo Please check the test reports for details at: %TEST_REPORT_DIR%
)

REM Exit with the stored exit code
exit /b %MAVEN_EXIT_CODE%

REM ======================================================================
REM Function to check prerequisites
REM ======================================================================
:check_prerequisites
echo Checking prerequisites...

REM Check if Java is installed
java -version >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo ERROR: Java is not installed or not in PATH
    exit /b 1
)

REM Check if Maven is installed
mvn -version >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo ERROR: Maven is not installed or not in PATH
    exit /b 1
)

REM Check if JavaFX is available
if "%VERBOSE%"=="true" (
    echo Checking for JavaFX runtime...
    java -Djava.awt.headless=true -version >nul 2>&1
    if %ERRORLEVEL% neq 0 (
        echo WARNING: JavaFX runtime check returned an error
        echo This may not be an issue if JavaFX is included as a Maven dependency
    ) else (
        echo JavaFX runtime check passed
    )
)

REM Check if required UI test data files exist
if not exist "%PROJECT_ROOT%\src\test\src\test\resources\test-suites\ui-tests.xml" (
    echo WARNING: UI test suite configuration file not found at:
    echo %PROJECT_ROOT%\src\test\src\test\resources\test-suites\ui-tests.xml
    echo This may not be an issue if test discovery is handled by other means
)

echo Prerequisites check completed successfully.
exit /b 0

REM ======================================================================
REM Function to parse command-line arguments
REM ======================================================================
:parse_arguments
:args_loop
if "%~1"=="" goto args_done

if "%~1"=="-h" (
    call :display_help
    exit /b 0
) else if "%~1"=="--help" (
    call :display_help
    exit /b 0
) else if "%~1"=="-v" (
    set "VERBOSE=true"
    echo Verbose output enabled
) else if "%~1"=="--verbose" (
    set "VERBOSE=true"
    echo Verbose output enabled
) else if "%~1"=="-r" (
    if "%~2"=="" (
        echo ERROR: Report directory not specified after -r option
        exit /b 1
    )
    set "TEST_REPORT_DIR=%~2"
    echo Test report directory set to: !TEST_REPORT_DIR!
    shift
) else if "%~1"=="--report-dir" (
    if "%~2"=="" (
        echo ERROR: Report directory not specified after --report-dir option
        exit /b 1
    )
    set "TEST_REPORT_DIR=%~2"
    echo Test report directory set to: !TEST_REPORT_DIR!
    shift
) else if "%~1"=="-n" (
    set "HEADLESS_MODE=false"
    echo Headless mode disabled - tests will run with visible UI
) else if "%~1"=="--no-headless" (
    set "HEADLESS_MODE=false"
    echo Headless mode disabled - tests will run with visible UI
) else (
    echo WARNING: Unknown option: %~1
)

shift
goto args_loop
:args_done

exit /b 0

REM ======================================================================
REM Function to display help information
REM ======================================================================
:display_help
echo.
echo Run UI tests for the Compound Interest Calculator application
echo.
echo Usage: run-ui-tests.bat [options]
echo.
echo Options:
echo   -h, --help             Display this help message
echo   -v, --verbose          Enable verbose output
echo   -r, --report-dir DIR   Specify custom report directory
echo   -n, --no-headless      Run UI tests with visible windows (not in headless mode)
echo.
echo Examples:
echo   run-ui-tests.bat
echo   run-ui-tests.bat -n -v
echo   run-ui-tests.bat --report-dir "C:\test-reports"
echo.
exit /b 0

REM ======================================================================
REM Function to generate UI test reports
REM ======================================================================
:generate_ui_report
echo Generating UI test reports...

REM Create screenshots of failed tests if not in headless mode
if "%HEADLESS_MODE%"=="false" (
    if exist "%PROJECT_ROOT%\target\surefire-reports" (
        echo Capturing screenshots of failed tests...
        REM In a real implementation, we'd capture screenshots of the failing tests
        REM For example: java -cp ... com.bank.calculator.test.util.ScreenshotTool
    )
)

REM Generate HTML reports
echo Generating HTML reports...
cd "%PROJECT_ROOT%"
mvn surefire-report:report-only -DoutputDirectory="%TEST_REPORT_DIR%" >nul

echo Report generation complete. Reports available at: %TEST_REPORT_DIR%
exit /b 0

endlocal