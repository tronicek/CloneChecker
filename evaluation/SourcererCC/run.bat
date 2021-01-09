@echo off
call ../../setEnv.bat

set BIGCLONEBENCH_DIR="/home/ubuntu/BigCloneBench/bcb_reduced/"

java %OPTIONS% -cp %TOOL_JAR% sourcerercc.NiCadConvertor bookkeeping.file sourcerercc-bigclonebench-40.txt %BIGCLONEBENCH_DIR%
java %OPTIONS% -cp %TOOL_JAR% drdup.Counter sourcerercc-bigclonebench-40.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Sourcer %SRC_DIR% sourcerercc-bigclonebench-40.xml

java %OPTIONS% -jar %CHECKER_JAR% checker.properties

echo "All clones"
java %OPTIONS% -cp %TOOL_JAR% drdup.Counter sourcerercc-bigclonebench-40.xml
echo "Type 1"
java %OPTIONS% -cp %TOOL_JAR% drdup.Counter output/clones-type1.xml
echo "Type 2"
java %OPTIONS% -cp %TOOL_JAR% drdup.Counter output/clones-type2.xml
echo "Invalid"
java %OPTIONS% -cp %TOOL_JAR% drdup.Counter output/invalid.xml
echo "Nonclones"
java %OPTIONS% -cp %TOOL_JAR% drdup.Counter output/not-clones.xml
