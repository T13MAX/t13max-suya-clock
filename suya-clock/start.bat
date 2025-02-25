@echo off

chcp 65001

setlocal

set INITIAL_MEMORY=-Xms128m

set MAX_MEMORY=-Xmx128m

set SCRIPT_DIR=%~dp0

set JRE_PATH=%SCRIPT_DIR%jdk/jdk-win/bin/java.exe

set LIBS_PATH=%SCRIPT_DIR%libs

set MAIN_CLASS=com.t13max.suyaclock.SuyaClockApplication

set CLASSPATH=%LIBS_PATH%/*

%JRE_PATH% -DSCRIPT_DIR=%SCRIPT_DIR% %INITIAL_MEMORY% %MAX_MEMORY% -cp %CLASSPATH% %MAIN_CLASS% -outputEncoding utf-8

endlocal
