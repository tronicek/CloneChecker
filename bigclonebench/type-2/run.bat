@echo off
call ..\setEnv.bat

java %OPTIONS% -cp %TOOL_JAR% drdup.Sourcer %SRC_DIR% type-2.xml
java %OPTIONS% -jar %CHECKER_JAR% checker.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Counter type-2.xml
