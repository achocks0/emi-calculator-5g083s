@echo off
setlocal enabledelayedexpansion

:: Compound Interest Calculator - Performance Tests Runner
:: This script runs performance tests for the Compound Interest Calculator application

:: Set default values
set "MAVEN_OPTS=-Xmx1024m"
set "TEST_REPORT_DIR=../target/test-reports/performance"
set "PERFORMANCE_TEST_PACKAGE=com.bank.calculator.test.performance"
set "PERFORMANCE_TAG=performance"
set "VERBOSE=false"
set "SPECIFIC_TEST="
set "CUSTOM_JVM_OPTS="

:: Process command-line arguments
call :parse_arguments %*

:: Check if help is requested
if defined SHOW_HELP (
    call :display_help
    exit /b 0
)

:: Check prerequisites
call :check_prerequisites
if %ERRORLEVEL% neq 0 (
    echo ERROR: Prerequisites check failed. Cannot run performance tests.
    exit /b %ERRORLEVEL%
)

:: Run the performance tests
call :run_performance_tests
exit /b %ERRORLEVEL%

:: Functions

:check_prerequisites
    echo Checking prerequisites...

    :: Check Java installation
    java -version >nul 2>&1
    if %ERRORLEVEL% neq 0 (
        echo ERROR: Java is not installed or not in PATH.
        echo Please install Java 11 or higher and ensure it's available in your PATH.
        exit /b 1
    )

    :: Check Maven installation
    mvn -v >nul 2>&1
    if %ERRORLEVEL% neq 0 (
        echo ERROR: Maven is not installed or not in PATH.
        echo Please install Maven 3.8.6 or higher and ensure it's available in your PATH.
        exit /b 2
    )

    :: Check test files exist
    if not exist "src\test\resources\test-suites\performance-tests.xml" (
        echo ERROR: Performance test suite configuration file not found.
        echo Expected at: src\test\resources\test-suites\performance-tests.xml
        exit /b 3
    )

    if not exist "src\test\resources\test-data\performance-thresholds.json" (
        echo ERROR: Performance thresholds configuration file not found.
        echo Expected at: src\test\resources\test-data\performance-thresholds.json
        exit /b 4
    )

    echo All prerequisites are met.
    exit /b 0

:run_performance_tests
    echo.
    echo ====================================================================
    echo  COMPOUND INTEREST CALCULATOR - PERFORMANCE TESTS
    echo ====================================================================
    echo.

    if "%VERBOSE%"=="true" (
        echo Running performance tests with the following configuration:
        echo  - Test package: %PERFORMANCE_TEST_PACKAGE%
        echo  - Test tag: %PERFORMANCE_TAG%
        echo  - Report directory: %TEST_REPORT_DIR%
        if defined SPECIFIC_TEST echo  - Specific test: %SPECIFIC_TEST%
        if defined CUSTOM_JVM_OPTS echo  - Custom JVM options: %CUSTOM_JVM_OPTS%
        echo.
    )

    :: Set environment variables
    if defined CUSTOM_JVM_OPTS (
        set "MAVEN_OPTS=%CUSTOM_JVM_OPTS%"
    )

    :: Ensure report directory exists
    if not exist "%TEST_REPORT_DIR%" mkdir "%TEST_REPORT_DIR%"

    :: Build Maven command
    set "MVN_CMD=mvn test"
    set "MVN_CMD=%MVN_CMD% -Dtest.package=%PERFORMANCE_TEST_PACKAGE%"
    set "MVN_CMD=%MVN_CMD% -Dgroups=%PERFORMANCE_TAG%"
    set "MVN_CMD=%MVN_CMD% -Dtest.report.dir=%TEST_REPORT_DIR%"
    
    if defined SPECIFIC_TEST (
        set "MVN_CMD=%MVN_CMD% -Dtest=%SPECIFIC_TEST%"
    )
    
    if "%VERBOSE%"=="true" (
        set "MVN_CMD=%MVN_CMD% -Dtest.verbose=true"
    ) else (
        set "MVN_CMD=%MVN_CMD% -Dtest.verbose=false"
    )

    :: Execute Maven command
    echo Executing: %MVN_CMD%
    echo.
    call %MVN_CMD%
    set EXIT_CODE=%ERRORLEVEL%

    :: Check if tests were successful
    if %EXIT_CODE% equ 0 (
        echo.
        echo ====================================================================
        echo  PERFORMANCE TESTS COMPLETED SUCCESSFULLY
        echo ====================================================================
        echo.
        echo Performance test reports are available at:
        echo %TEST_REPORT_DIR%
        echo.
    ) else (
        echo.
        echo ====================================================================
        echo  PERFORMANCE TESTS FAILED
        echo ====================================================================
        echo.
        echo Please review the test output above for details on the failure.
        echo.
    )

    exit /b %EXIT_CODE%

:parse_arguments
    :arg_loop
    if "%~1"=="" goto :arg_end

    if "%~1"=="-h" (
        set "SHOW_HELP=true"
    ) else if "%~1"=="--help" (
        set "SHOW_HELP=true"
    ) else if "%~1"=="-v" (
        set "VERBOSE=true"
    ) else if "%~1"=="--verbose" (
        set "VERBOSE=true"
    ) else if "%~1"=="-r" (
        if "%~2"=="" (
            echo ERROR: Missing value for %~1 option
            exit /b 1
        )
        set "TEST_REPORT_DIR=%~2"
        shift
    ) else if "%~1"=="--report-dir" (
        if "%~2"=="" (
            echo ERROR: Missing value for %~1 option
            exit /b 1
        )
        set "TEST_REPORT_DIR=%~2"
        shift
    ) else if "%~1"=="-t" (
        if "%~2"=="" (
            echo ERROR: Missing value for %~1 option
            exit /b 1
        )
        set "SPECIFIC_TEST=%~2"
        shift
    ) else if "%~1"=="--test" (
        if "%~2"=="" (
            echo ERROR: Missing value for %~1 option
            exit /b 1
        )
        set "SPECIFIC_TEST=%~2"
        shift
    ) else if "%~1"=="-j" (
        if "%~2"=="" (
            echo ERROR: Missing value for %~1 option
            exit /b 1
        )
        set "CUSTOM_JVM_OPTS=%~2"
        shift
    ) else if "%~1"=="--jvm-opts" (
        if "%~2"=="" (
            echo ERROR: Missing value for %~1 option
            exit /b 1
        )
        set "CUSTOM_JVM_OPTS=%~2"
        shift
    ) else (
        echo WARNING: Unknown option: %~1
    )
    
    shift
    goto :arg_loop
    
    :arg_end
    exit /b 0

:display_help
    echo.
    echo COMPOUND INTEREST CALCULATOR - PERFORMANCE TESTS RUNNER
    echo.
    echo This script runs performance tests for the Compound Interest Calculator
    echo application to verify it meets performance requirements.
    echo.
    echo USAGE:
    echo   run-performance-tests.bat [options]
    echo.
    echo OPTIONS:
    echo   -h, --help                Display this help information
    echo   -v, --verbose             Enable verbose output
    echo   -r, --report-dir DIR      Specify custom report directory
    echo   -t, --test TEST_NAME      Run specific performance test class or method
    echo   -j, --jvm-opts OPTIONS    Specify custom JVM options for Maven
    echo.
    echo EXAMPLES:
    echo   run-performance-tests.bat
    echo   run-performance-tests.bat --verbose
    echo   run-performance-tests.bat --test CalculationPerformanceTest
    echo   run-performance-tests.bat --jvm-opts "-Xmx2048m -XX:+UseG1GC"
    echo.
    exit /b 0