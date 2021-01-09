@echo off
call ../../setEnv.bat

java %OPTIONS% -jar %DRDUPLEX_JAR% bigclonebench.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator drduplex-bigclonebench.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Sourcer %SRC_DIR% drduplex-bigclonebench-separated.xml

java %OPTIONS% -jar %CHECKER_JAR% checker.properties

echo "All clones"
java %OPTIONS% -cp %TOOL_JAR% drdup.Counter drduplex-bigclonebench-separated.xml
echo "Type 1"
java %OPTIONS% -cp %TOOL_JAR% drdup.Counter output/clones-type1.xml
echo "Type 2"
java %OPTIONS% -cp %TOOL_JAR% drdup.Counter output/clones-type2.xml
echo "Invalid"
java %OPTIONS% -cp %TOOL_JAR% drdup.Counter output/invalid.xml
echo "Nonclones"
java %OPTIONS% -cp %TOOL_JAR% drdup.Counter output/not-clones.xml
