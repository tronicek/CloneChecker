@echo off
call ../../setEnv.bat

set BIGCLONEBENCH_SRC_DIR="/home/ubuntu/BigCloneBench/bcb_reduced/"

java -cp %TOOL_JAR% drdup.ChangeDir %BIGCLONEBENCH_SRC_DIR% bigclonebench-blind-minsize-6.xml

java %OPTIONS% -cp %TOOL_JAR% drdup.Sourcer %SRC_DIR% bigclonebench-blind-minsize-6-dir.xml

java %OPTIONS% -jar %CHECKER_JAR% checker.properties

echo "All clones"
java %OPTIONS% -cp %TOOL_JAR% drdup.Counter bigclonebench-blind-minsize-6.xml
echo "Type 1"
java %OPTIONS% -cp %TOOL_JAR% drdup.Counter output/clones-type1.xml
echo "Type 2"
java %OPTIONS% -cp %TOOL_JAR% drdup.Counter output/clones-type2.xml
echo "Invalid"
java %OPTIONS% -cp %TOOL_JAR% drdup.Counter output/invalid.xml
echo "Nonclones"
java %OPTIONS% -cp %TOOL_JAR% drdup.Counter output/not-clones.xml
