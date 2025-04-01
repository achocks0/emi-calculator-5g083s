@echo off
setlocal enabledelayedexpansion

:: ========================================================================
:: Compound Interest Calculator - Test Runner Script
:: 
:: This script executes all test categories (unit, integration, UI, 
:: performance, and security) for the Compound Interest Calculator.
:: 
:: It provides a convenient way to run the complete test suite from the command line.
:: ========================================================================

:: Define global variables
set MAVEN_OPTS=-Xmx1024m
set TEST_REPORT_DIR=../target/test-reports
set SCRIPT_DIR=%~dp0
set PROJECT_ROOT=%SCRIPT_DIR%../..
set TEST_CATEGORIES=unit,integration,ui,performance,security
set VERBOSE=false
set JVM_OPTS=
set SHOW_HELP=false

echo Compound Interest Calculator - Test Runner

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

:: Run the tests
call :run_all_tests
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
if not exist "%PROJECT_ROOT%\src\test\src\main\java\com\bank\calculator\test\runner\AllTestsRunner.java" (
    echo ERROR: Required test runner class not found.
    echo Looking for: %PROJECT_ROOT%\src\test\src\main\java\com\bank\calculator\test\runner\AllTestsRunner.java
    exit /b 1
)

if not exist "%PROJECT_ROOT%\src\test\src\test\resources\test-suites\all-tests.xml" (
    echo ERROR: Required test suite configuration not found.
    echo Looking for: %PROJECT_ROOT%\src\test\src\test\resources\test-suites\all-tests.xml
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
if /i "%~1"=="-c" set TEST_CATEGORIES=%~2& shift & goto :next_arg
if /i "%~1"=="--categories" set TEST_CATEGORIES=%~2& shift & goto :next_arg
if /i "%~1"=="-j" set JVM_OPTS=%~2& shift & goto :next_arg
if /i "%~1"=="--jvm-opts" set JVM_OPTS=%~2& shift & goto :next_arg

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
    echo   Test Categories: %TEST_CATEGORIES%
    echo   JVM Options: %JVM_OPTS%
)
exit /b 0

:display_help
echo.
echo Compound Interest Calculator - Test Runner Script
echo.
echo This script runs all tests for the Compound Interest Calculator application.
echo.
echo Usage: run-tests.bat [options]
echo.
echo Options:
echo   -h, --help                 Display this help information
echo   -v, --verbose              Enable verbose output
echo   -r, --report-dir DIR       Specify custom report directory
echo   -c, --categories CATS      Specify test categories to run (comma-separated)
echo                             Available: unit,integration,ui,performance,security
echo   -j, --jvm-opts OPTS        Specify custom JVM options for Maven
echo.
echo Examples:
echo   run-tests.bat                     Run all tests
echo   run-tests.bat -c unit,integration Run only unit and integration tests
echo   run-tests.bat -v                  Run all tests with verbose output
echo   run-tests.bat -r ./my-reports     Save reports to ./my-reports directory
echo.
exit /b 0

:run_all_tests
echo.
echo ========================================================================
echo                Running Compound Interest Calculator Tests
echo                Categories: %TEST_CATEGORIES%
echo ========================================================================
echo.

:: Set environment variables
if defined JVM_OPTS (
    set MAVEN_OPTS=%MAVEN_OPTS% %JVM_OPTS%
)
if "%VERBOSE%"=="true" echo Setting MAVEN_OPTS=%MAVEN_OPTS%

:: Create test report directory if it doesn't exist
if not exist "%TEST_REPORT_DIR%" (
    mkdir "%TEST_REPORT_DIR%"
    if "%VERBOSE%"=="true" echo Created test report directory: %TEST_REPORT_DIR%
)

:: Determine verbosity level for Maven
set MVN_VERBOSITY=-q
if "%VERBOSE%"=="true" set MVN_VERBOSITY=

:: Run the tests
echo Running tests...
echo.

:: Define test categories as Maven properties
set TEST_PROPS=
for %%c in (%TEST_CATEGORIES:,= %) do (
    set TEST_PROPS=!TEST_PROPS! -Dtest.category.%%c=true
)

:: Execute Maven command to run tests
cd "%PROJECT_ROOT%"
if "%VERBOSE%"=="true" (
    echo Executing: mvn %MVN_VERBOSITY% clean test -Dtest.categories=%TEST_CATEGORIES% %TEST_PROPS% -Dtest.report.dir="%TEST_REPORT_DIR%" -Djunit.jupiter.execution.parallel.enabled=true
)

mvn %MVN_VERBOSITY% clean test -Dtest.categories=%TEST_CATEGORIES% %TEST_PROPS% -Dtest.report.dir="%TEST_REPORT_DIR%" -Djunit.jupiter.execution.parallel.enabled=true
set MVN_RESULT=%ERRORLEVEL%

:: Generate a consolidated report
call :generate_report

:: Display results
echo.
if %MVN_RESULT% EQU 0 (
    echo ========================================================================
    echo                     ALL TESTS PASSED SUCCESSFULLY!
    echo ========================================================================
) else (
    echo ========================================================================
    echo                     SOME TESTS FAILED! CHECK THE REPORT.
    echo                     Report location: %TEST_REPORT_DIR%
    echo ========================================================================
)

exit /b %MVN_RESULT%

:generate_report
echo.
echo Generating consolidated test report...

:: First check if there's anything to report
if not exist "%TEST_REPORT_DIR%" (
    echo No test results found.
    exit /b 0
)

:: Generate HTML reports using Maven site plugin
cd "%PROJECT_ROOT%"
mvn site:site -DgenerateReports=false -DoutputDirectory="%TEST_REPORT_DIR%/site" %MVN_VERBOSITY%

:: Copy the JUnit and Surefire reports to the report directory
if exist "target/surefire-reports" (
    echo Copying Surefire reports...
    xcopy /E /I /Y "target/surefire-reports" "%TEST_REPORT_DIR%\surefire-reports" > nul
)

echo.
echo Test reports generated in: %TEST_REPORT_DIR%
echo Detailed HTML report:      %TEST_REPORT_DIR%/site/index.html
echo JUnit XML reports:         %TEST_REPORT_DIR%/surefire-reports
echo.

exit /b 0