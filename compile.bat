@echo off

cd /d "%~dp0"
javac -d bin src\*.java
echo Compile done

pause