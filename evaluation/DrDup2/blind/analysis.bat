@echo off
call ../../setEnv.bat

java %OPTIONS% -cp %TOOL_JAR% drdup.Sourcer %SRC_DIR% drdup2-cloneworks.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Sourcer %SRC_DIR% drdup2-nicad.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Sourcer %SRC_DIR% drdup2-sourcerercc.xml

java %OPTIONS% -jar %CHECKER_JAR% checker-nicad.properties
java %OPTIONS% -jar %CHECKER_JAR% checker-cloneworks.properties
java %OPTIONS% -jar %CHECKER_JAR% checker-sourcerercc.properties

echo "Missed by NiCad"
java %OPTIONS% -cp %TOOL_JAR% drdup.Counter output-nicad/all.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Counter output-nicad/clones-type1.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Counter output-nicad/clones-type2.xml

echo "Missed by CloneWorks"
java %OPTIONS% -cp %TOOL_JAR% drdup.Counter output-cloneworks/all.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Counter output-cloneworks/clones-type1.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Counter output-cloneworks/clones-type2.xml

echo "Missed by SourcererCC"
java %OPTIONS% -cp %TOOL_JAR% drdup.Counter output-sourcerercc/all.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Counter output-sourcerercc/clones-type1.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Counter output-sourcerercc/clones-type2.xml
