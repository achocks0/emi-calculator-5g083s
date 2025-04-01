@echo off
setlocal enabledelayedexpansion

:: ========================================================================
:: Compound Interest Calculator - Integration Tests Runner Script
:: 
:: This script executes integration tests for the Compound Interest Calculator
:: application. It provides a convenient way to run integration tests from the
:: command line on Windows systems.
:: ========================================================================

:: Define global variables
set MAVEN_OPTS=-Xmx512m -Dspring.profiles.active=integration
set TEST_REPORT_DIR=..\target\integration-test-reports
set SCRIPT_DIR=%~dp0
set PROJECT_ROOT=%SCRIPT_DIR%..\..\..
set VERBOSE=false

echo Compound Interest Calculator - Integration Tests Runner

:: Parse command line arguments
call :parse_arguments %*

:: Display help if requested
if "%SHOW_HELP%"=="true" (
    call :display_help
    exit /b 0
)

:: Check prerequisites
call :check_prerequisites
if %ERRORLEVEL% neq 0 (
    echo ERROR: Failed to verify prerequisites.
    exit /b 1
)

:: Run the integration tests
call :run_integration_tests
exit /b %ERRORLEVEL%

:: ========================================================================
:: Functions
:: ========================================================================

:check_prerequisites
if "%VERBOSE%"=="true" echo Checking prerequisites...

:: Check if Java is installed
where java >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo ERROR: Java not found. Please install Java and add it to your PATH.
    exit /b 1
)
if "%VERBOSE%"=="true" (
    java -version
)

:: Check if Maven is installed
where mvn >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo ERROR: Maven not found. Please install Maven and add it to your PATH.
    exit /b 1
)
if "%VERBOSE%"=="true" (
    mvn --version
)

:: Check if required test files exist
if not exist "%PROJECT_ROOT%\src\test\src\main\java\com\bank\calculator\test\runner\IntegrationTestsRunner.java" (
    echo ERROR: Required test runner class not found.
    echo Looking for: %PROJECT_ROOT%\src\test\src\main\java\com\bank\calculator\test\runner\IntegrationTestsRunner.java
    exit /b 1
)

if not exist "%PROJECT_ROOT%\src\test\src\test\resources\test-suites\integration-tests.xml" (
    echo ERROR: Required test suite configuration not found.
    echo Looking for: %PROJECT_ROOT%\src\test\src\test\resources\test-suites\integration-tests.xml
    exit /b 1
)

if "%VERBOSE%"=="true" echo Prerequisites check completed successfully.
exit /b 0

:parse_arguments
:: Loop through all arguments
:parse_loop
if "%~1"=="" goto :parse_done
if /i "%~1"=="-h" set SHOW_HELP=true& goto :next_arg
if /i "%~1"=="--help" set SHOW_HELP=true& goto :next_arg
if /i "%~1"=="-v" set VERBOSE=true& goto :next_arg
if /i "%~1"=="--verbose" set VERBOSE=true& goto :next_arg
if /i "%~1"=="-r" set TEST_REPORT_DIR=%~2& shift & goto :next_arg
if /i "%~1"=="--report-dir" set TEST_REPORT_DIR=%~2& shift & goto :next_arg

echo Unknown option: %~1
call :display_help
exit /b 1

:next_arg
shift
goto :parse_loop

:parse_done
if "%VERBOSE%"=="true" (
    echo Command-line arguments:
    echo   Verbose: %VERBOSE%
    echo   Report Directory: %TEST_REPORT_DIR%
)
exit /b 0

:display_help
echo.
echo Compound Interest Calculator - Integration Tests Runner Script
echo.
echo This script runs integration tests for the Compound Interest Calculator application.
echo.
echo Usage: run-integration-tests.bat [options]
echo.
echo Options:
echo   -h, --help                 Display this help information
echo   -v, --verbose              Enable verbose output
echo   -r, --report-dir DIR       Specify custom report directory
echo.
echo Examples:
echo   run-integration-tests.bat             Run integration tests
echo   run-integration-tests.bat -v          Run integration tests with verbose output
echo   run-integration-tests.bat -r ./reports    Save reports to ./reports directory
echo.
exit /b 0

:run_integration_tests
echo.
echo ========================================================================
echo                Running Integration Tests
echo ========================================================================
echo.

:: Set environment variables
if "%VERBOSE%"=="true" echo Setting MAVEN_OPTS=%MAVEN_OPTS%

:: Create test report directory if it doesn't exist
if not exist "%TEST_REPORT_DIR%" (
    mkdir "%TEST_REPORT_DIR%"
    if "%VERBOSE%"=="true" echo Created test report directory: %TEST_REPORT_DIR%
)

:: Determine verbosity level for Maven
set MVN_VERBOSITY=-q
if "%VERBOSE%"=="true" set MVN_VERBOSITY=

:: Execute Maven command to run integration tests
cd "%PROJECT_ROOT%"
if "%VERBOSE%"=="true" (
    echo Executing Maven to run integration tests...
)

:: Run the tests using Maven with the integration category
mvn %MVN_VERBOSITY% clean test -Dtest.category=integration -Dtest.report.dir="%TEST_REPORT_DIR%" -Dspring.profiles.active=integration -Djunit.jupiter.execution.parallel.enabled=true
set MVN_RESULT=%ERRORLEVEL%

:: Display results
echo.
if %MVN_RESULT% EQU 0 (
    echo ========================================================================
    echo                     INTEGRATION TESTS PASSED SUCCESSFULLY!
    echo ========================================================================
) else (
    echo ========================================================================
    echo                     INTEGRATION TESTS FAILED! CHECK THE REPORT.
    echo                     Report location: %TEST_REPORT_DIR%
    echo ========================================================================
)

exit /b %MVN_RESULT%