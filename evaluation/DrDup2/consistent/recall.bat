@echo off
call ../../setEnv.bat

set CLONEWORKS="/research/projects/CloneChecker/evaluation/CloneWorks/consistent/cloneworks-bigclonebench-consistent.xml"
set NICAD="/research/projects/CloneChecker/evaluation/NiCad/consistent/bigclonebench-consistent-minsize-6-dir.xml"

java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup2-bigclonebench-separated.xml %CLONEWORKS% drdup2-cloneworks.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup2-bigclonebench-separated.xml %NICAD% drdup2-nicad.xml

echo "NiCad"
java %OPTIONS% -cp %TOOL_JAR% drdup.Counter drdup2-nicad.xml
echo "CloneWorks"
java %OPTIONS% -cp %TOOL_JAR% drdup.Counter drdup2-cloneworks.xml
echo "DrDup2"
java %OPTIONS% -cp %TOOL_JAR% drdup.Counter drdup2-bigclonebench-separated.xml
