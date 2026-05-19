@echo off
setlocal

if not exist out mkdir out

javac -encoding UTF-8 -d out ^
  src\main\controller\*.java ^
  src\main\enumm\*.java ^
  src\main\exception\*.java ^
  src\main\model\common\*.java ^
  src\main\model\course\*.java ^
  src\main\model\research\*.java ^
  src\main\model\user\*.java ^
  src\main\repository\*.java ^
  src\main\service\*.java

if errorlevel 1 (
  echo Compilation failed.
  exit /b 1
)

java -cp out controller.MainController
