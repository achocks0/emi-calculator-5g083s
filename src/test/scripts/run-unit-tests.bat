@echo off
:: ============================================================================
:: Compound Interest Calculator - Unit Tests Runner
:: ============================================================================
:: This batch script runs unit tests for the Compound Interest Calculator
:: application using Maven and JUnit 5.
::
:: Author: Bank Calculator Team
:: Version: 1.0
:: Date: 2023
:: ============================================================================

setlocal enabledelayedexpansion

:: Set script directory and project root
set "SCRIPT_DIR=%~dp0"
set "PROJECT_ROOT=%SCRIPT_DIR%..\.."

:: Default values
set "MAVEN_OPTS=-Xmx512m"
set "TEST_REPORT_DIR=%PROJECT_ROOT%\target\test-reports\unit"
set "VERBOSE=false"
set "CUSTOM_JVM_OPTS="
set "USE_RUNNER=false"

:: Parse command-line arguments
call :parse_arguments %*
if %ERRORLEVEL% neq 0 exit /b %ERRORLEVEL%

:: Display banner
echo.
echo ============================================================================
echo                   RUNNING UNIT TESTS - COMPOUND INTEREST CALCULATOR
echo ============================================================================
echo.

:: Check prerequisites
call :check_prerequisites
if %ERRORLEVEL% neq 0 (
    echo ERROR: Prerequisites check failed. Cannot run unit tests.
    exit /b 2
)

:: Set up the environment
call :setup_environment

:: Decide how to run tests based on USE_RUNNER flag
if "%USE_RUNNER%"=="true" (
    call :run_tests_with_runner
) else (
    call :run_tests_with_maven
)

:: Exit with the result from the test run
exit /b %ERRORLEVEL%

:: ============================================================================
:: Function to run tests using Maven
:: ============================================================================
:run_tests_with_maven
    echo Running unit tests with Maven...
    if "%VERBOSE%"=="true" (
        echo Command: mvn test -Dtest.category=unit -Dsurefire.suiteXmlFiles=src/test/src/test/resources/test-suites/unit-tests.xml -Dtest.report.dir="%TEST_REPORT_DIR%"
        echo.
    )

    mvn test -Dtest.category=unit -Dsurefire.suiteXmlFiles=src/test/src/test/resources/test-suites/unit-tests.xml -Dtest.report.dir="%TEST_REPORT_DIR%"
    set "TEST_RESULT=%ERRORLEVEL%"

    if %TEST_RESULT% neq 0 (
        echo.
        echo ============================================================================
        echo                           UNIT TESTS FAILED
        echo ============================================================================
        echo.
        echo See test reports at: %TEST_REPORT_DIR%
        exit /b 1
    ) else (
        echo.
        echo ============================================================================
        echo                           UNIT TESTS PASSED
        echo ============================================================================
        echo.
        echo See test reports at: %TEST_REPORT_DIR%
        exit /b 0
    )

:: ============================================================================
:: Function to run tests using UnitTestsRunner directly
:: ============================================================================
:run_tests_with_runner
    echo Running unit tests with UnitTestsRunner...
    
    :: Build classpath
    set "CP=%PROJECT_ROOT%\target\classes;%PROJECT_ROOT%\target\test-classes"
    
    :: Add all dependencies from Maven repo
    set "CP=%CP%;%USERPROFILE%\.m2\repository\org\junit\jupiter\junit-jupiter-api\5.8.2\junit-jupiter-api-5.8.2.jar"
    set "CP=%CP%;%USERPROFILE%\.m2\repository\org\junit\platform\junit-platform-launcher\1.8.2\junit-platform-launcher-1.8.2.jar"
    set "CP=%CP%;%USERPROFILE%\.m2\repository\org\junit\platform\junit-platform-engine\1.8.2\junit-platform-engine-1.8.2.jar"
    set "CP=%CP%;%USERPROFILE%\.m2\repository\org\junit\platform\junit-platform-commons\1.8.2\junit-platform-commons-1.8.2.jar"
    set "CP=%CP%;%USERPROFILE%\.m2\repository\org\junit\jupiter\junit-jupiter-engine\5.8.2\junit-jupiter-engine-5.8.2.jar"
    
    :: Set system properties for test report directory
    set "PROPS=-Dtest.report.dir=%TEST_REPORT_DIR%"
    
    if "%VERBOSE%"=="true" (
        echo Command: java %MAVEN_OPTS% -cp "%CP%" %PROPS% com.bank.calculator.test.runner.UnitTestsRunner
        echo.
    )
    
    java %MAVEN_OPTS% -cp "%CP%" %PROPS% com.bank.calculator.test.runner.UnitTestsRunner
    set "TEST_RESULT=%ERRORLEVEL%"
    
    if %TEST_RESULT% neq 0 (
        echo.
        echo ============================================================================
        echo                           UNIT TESTS FAILED
        echo ============================================================================
        echo.
        echo See test reports at: %TEST_REPORT_DIR%
        exit /b 1
    ) else (
        echo.
        echo ============================================================================
        echo                           UNIT TESTS PASSED
        echo ============================================================================
        echo.
        echo See test reports at: %TEST_REPORT_DIR%
        exit /b 0
    )

:: ============================================================================
:: Function to parse command-line arguments
:: ============================================================================
:parse_arguments
    :arg_loop
    if "%~1" == "" goto :arg_done
    
    if "%~1" == "-h" (
        call :display_help
        exit /b 3
    )
    if "%~1" == "--help" (
        call :display_help
        exit /b 3
    )
    
    if "%~1" == "-v" (
        set "VERBOSE=true"
        shift
        goto :arg_loop
    )
    if "%~1" == "--verbose" (
        set "VERBOSE=true"
        shift
        goto :arg_loop
    )
    
    if "%~1" == "-r" (
        if "%~2" == "" (
            echo ERROR: Missing value for report directory.
            exit /b 3
        )
        set "TEST_REPORT_DIR=%~2"
        shift
        shift
        goto :arg_loop
    )
    if "%~1" == "--report-dir" (
        if "%~2" == "" (
            echo ERROR: Missing value for report directory.
            exit /b 3
        )
        set "TEST_REPORT_DIR=%~2"
        shift
        shift
        goto :arg_loop
    )
    
    if "%~1" == "-j" (
        if "%~2" == "" (
            echo ERROR: Missing value for JVM options.
            exit /b 3
        )
        set "CUSTOM_JVM_OPTS=%~2"
        shift
        shift
        goto :arg_loop
    )
    if "%~1" == "--jvm-opts" (
        if "%~2" == "" (
            echo ERROR: Missing value for JVM options.
            exit /b 3
        )
        set "CUSTOM_JVM_OPTS=%~2"
        shift
        shift
        goto :arg_loop
    )
    
    if "%~1" == "--runner" (
        set "USE_RUNNER=true"
        shift
        goto :arg_loop
    )
    
    echo ERROR: Unknown option: %~1
    call :display_help
    exit /b 3
    
    :arg_done
    exit /b 0

:: ============================================================================
:: Function to display help information
:: ============================================================================
:display_help
    echo.
    echo Usage: run-unit-tests.bat [options]
    echo.
    echo This script runs unit tests for the Compound Interest Calculator application.
    echo.
    echo Options:
    echo   -h, --help                Show this help message
    echo   -v, --verbose             Enable verbose output
    echo   -r, --report-dir DIR      Specify custom report directory
    echo   -j, --jvm-opts OPTS       Specify custom JVM options for Maven
    echo   --runner                  Use UnitTestsRunner directly instead of Maven
    echo.
    echo Examples:
    echo   run-unit-tests.bat
    echo   run-unit-tests.bat --verbose
    echo   run-unit-tests.bat --report-dir C:\reports\unit
    echo   run-unit-tests.bat --jvm-opts "-Xmx1024m -XX:MaxPermSize=256m"
    echo   run-unit-tests.bat --runner
    echo.
    exit /b 0

:: ============================================================================
:: Function to check prerequisites
:: ============================================================================
:check_prerequisites
    :: Check Java installation
    where java >nul 2>&1
    if %ERRORLEVEL% neq 0 (
        echo ERROR: Java not found in PATH. Please ensure Java is installed and in your PATH.
        exit /b 1
    )

    :: Check Maven installation
    where mvn >nul 2>&1
    if %ERRORLEVEL% neq 0 (
        echo ERROR: Maven not found in PATH. Please ensure Maven is installed and in your PATH.
        exit /b 1
    )
    
    :: Check if the project directory exists
    if not exist "%PROJECT_ROOT%" (
        echo ERROR: Project root directory not found: %PROJECT_ROOT%
        exit /b 1
    )
    
    :: Check if the test suite file exists
    if not exist "%PROJECT_ROOT%\src\test\src\test\resources\test-suites\unit-tests.xml" (
        echo ERROR: Unit test suite file not found.
        exit /b 1
    )
    
    :: Check if the UnitTestsRunner class exists when using direct runner
    if "%USE_RUNNER%"=="true" (
        if not exist "%PROJECT_ROOT%\src\test\src\main\java\com\bank\calculator\test\runner\UnitTestsRunner.java" (
            echo ERROR: UnitTestsRunner class not found.
            exit /b 1
        )
    )
    
    :: All prerequisites met
    exit /b 0

:: ============================================================================
:: Function to set up the environment
:: ============================================================================
:setup_environment
    :: Set MAVEN_OPTS environment variable
    if not "%CUSTOM_JVM_OPTS%" == "" (
        set "MAVEN_OPTS=%MAVEN_OPTS% %CUSTOM_JVM_OPTS%"
    )
    
    if "%VERBOSE%"=="true" (
        echo MAVEN_OPTS: %MAVEN_OPTS%
        echo PROJECT_ROOT: %PROJECT_ROOT%
        echo TEST_REPORT_DIR: %TEST_REPORT_DIR%
        if "%USE_RUNNER%"=="true" echo Using UnitTestsRunner directly
    )
    
    :: Create test report directory if it doesn't exist
    if not exist "%TEST_REPORT_DIR%" (
        if "%VERBOSE%"=="true" echo Creating test report directory: %TEST_REPORT_DIR%
        mkdir "%TEST_REPORT_DIR%" 2>nul
    )
    
    exit /b 0

endlocal