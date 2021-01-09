@echo off
call ../../setEnv.bat

set CLONEWORKS="/research/projects/CloneChecker/evaluation/CloneWorks/blind/cloneworks-bigclonebench-blind.xml"
set NICAD="/research/projects/CloneChecker/evaluation/NiCad/blind/bigclonebench-blind-minsize-6-dir.xml"
set SOURCERERCC="/research/projects/CloneChecker/evaluation/SourcererCC/sourcerercc-bigclonebench-40.xml"

java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup2-bigclonebench-separated.xml %CLONEWORKS% drdup2-cloneworks.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup2-bigclonebench-separated.xml %NICAD% drdup2-nicad.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup2-bigclonebench-separated.xml %SOURCERERCC% drdup2-sourcerercc.xml

echo "DrDup2"
java %OPTIONS% -cp %TOOL_JAR% drdup.Counter drdup2-bigclonebench-separated.xml
echo "CloneWorks"
java %OPTIONS% -cp %TOOL_JAR% drdup.Counter drdup2-cloneworks.xml
echo "NiCad"
java %OPTIONS% -cp %TOOL_JAR% drdup.Counter drdup2-nicad.xml
echo "SourcererCC"
java %OPTIONS% -cp %TOOL_JAR% drdup.Counter drdup2-sourcerercc.xml

java %OPTIONS% -cp %TOOL_JAR% drdup.Sourcer %SRC_DIR% drdup2-cloneworks.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Sourcer %SRC_DIR% drdup2-nicad.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Sourcer %SRC_DIR% drdup2-sourcerercc.xml
