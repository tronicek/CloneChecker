@echo off
call ..\setEnv.bat

echo "All"
java %OPTIONS% -cp %TOOL_JAR% drdup.Counter type-12.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Counter output/all.xml

echo "Type 1"
java %OPTIONS% -cp %TOOL_JAR% drdup.Counter output/clones-type1.xml

echo "Type 2"
java %OPTIONS% -cp %TOOL_JAR% drdup.Counter output/clones-type2.xml

echo "Invalid"
java %OPTIONS% -cp %TOOL_JAR% drdup.Counter output/invalid.xml

echo "Nonclones"
java %OPTIONS% -cp %TOOL_JAR% drdup.Counter output/not-clones.xml
