@echo off
call ../../setEnv.bat

set CLONEWORKS="/research/projects/CloneChecker/evaluation/CloneWorks/blind/cloneworks-bigclonebench-blind.xml"
set NICAD="/research/projects/CloneChecker/evaluation/NiCad/blind/bigclonebench-blind-minsize-6-dir.xml"
set SOURCERERCC="/research/projects/CloneChecker/evaluation/SourcererCC/sourcerercc-bigclonebench-40.xml"

java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drduplex-bigclonebench-separated.xml %CLONEWORKS% drduplex-cloneworks.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drduplex-bigclonebench-separated.xml %NICAD% drduplex-nicad.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drduplex-bigclonebench-separated.xml %SOURCERERCC% drduplex-sourcerercc.xml

echo "DrDupLex"
java %OPTIONS% -cp %TOOL_JAR% drdup.Counter drduplex-bigclonebench-separated.xml
echo "CloneWorks"
java %OPTIONS% -cp %TOOL_JAR% drdup.Counter drduplex-cloneworks.xml
echo "NiCad"
java %OPTIONS% -cp %TOOL_JAR% drdup.Counter drduplex-nicad.xml
echo "SourcererCC"
java %OPTIONS% -cp %TOOL_JAR% drdup.Counter drduplex-sourcerercc.xml
