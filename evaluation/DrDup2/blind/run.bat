@echo off
call ../../setEnv.bat

java %OPTIONS% -jar %DRDUP2_JAR% bigclonebench.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator drdup2-bigclonebench.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Sourcer %SRC_DIR% drdup2-bigclonebench-separated.xml

java %OPTIONS% -jar %CHECKER_JAR% checker.properties

echo "All clones"
java %OPTIONS% -cp %TOOL_JAR% drdup.Counter drdup2-bigclonebench-separated.xml
echo "Type 1"
java %OPTIONS% -cp %TOOL_JAR% drdup.Counter output/clones-type1.xml
echo "Type 2"
java %OPTIONS% -cp %TOOL_JAR% drdup.Counter output/clones-type2.xml
echo "Invalid"
java %OPTIONS% -cp %TOOL_JAR% drdup.Counter output/invalid.xml
echo "Nonclones"
java %OPTIONS% -cp %TOOL_JAR% drdup.Counter output/not-clones.xml
