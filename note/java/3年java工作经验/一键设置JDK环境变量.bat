@echo off
color 2e
echo.-----------------------------------------------------------
echo.请选择使用JDK的版本：
echo.
echo. 1.使用JDK1.6（即在下面输入1）
echo.
echo. 2.使用JDK1.7（即在下面输入2）
echo.
echo. 3.使用JDK1.8（即在下面输入3）
echo.-----------------------------------------------------------

set /p version=请输入数字并按回车键确认:
if %version% == 1 goto jdk6
if %version% == 2 goto jdk7
if %version% == 3 goto jdk8
cls
set choice=
echo 您的输入有问题!
goto end

::设置环境变量为jdk1.6
:jdk6
set javahome=C:\Users\pillow\Desktop\grg\jdk\jdk1.6.0_43
set jdkVersion=1.6
goto set env

::设置环境变量为jdk1.7
:jdk7
set javahome=C:\Users\pillow\Desktop\grg\jdk\jdk7\jdk1.7.0_80
set jdkVersion=1.7
goto set env

::设置环境变量为jdk1.9
:jdk8
set javahome=C:\Users\pillow\Desktop\grg\jdk\jdk1.8.0_111
set jdkVersion=1.8
goto set env

:set env
set regpath=HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Control\Session Manager\Environment  
cls
echo.  
echo ************************************************************  
echo *                                                          *  
echo *                   JDK 系统环境变量设置                   *  
echo *                                                          *  
echo ************************************************************  
echo.  
echo === 准备设置环境变量: JAVA_HOME=%javahome%  
echo === 注意: 如果JAVA_HOME存在,会被覆盖,此操作不可逆的,请仔细检查确认!! ===  
echo.  
echo === 准备设置环境变量(后面有个.): classPath=%%JAVA_HOME%%\lib\tools.jar;%%JAVA_HOME%%\lib\dt.jar;.  
echo === 注意: 如果classPath存在,会被覆盖,此操作不可逆的,请仔细检查确认!! ===  
echo.  
echo === 准备设置环境变量: PATH=%%JAVA_HOME%%\bin  
echo === 注意: PATH会追加在最前面,  
echo.  
set /P EN=请确认后按 回车键 开始设置!  
echo.  
echo.  设置JAVA环境变量为%jdkVersion%开始:
echo === 新创建环境变量 JAVA_HOME=%javahome%  
setx "JAVA_HOME" "%javahome%" -M  
echo.  
echo === 新创建环境变量 classPath=%%JAVA_HOME%%\lib\tools.jar;%%JAVA_HOME%%%\lib\dt.jar;.  
setx "classPath" "%%JAVA_HOME%%\lib\tools.jar;%%JAVA_HOME%%%\lib\dt.jar;." -m  
echo.  
echo === 新追加环境变量(追加到最前面) PATH=%%JAVA_HOME%%\bin  
for /f "tokens=1,* delims=:" %%a in ('reg QUERY "%regpath%" /v "path"') do (  
    set "L=%%a"  
    set "P=%%b"  
)  
set "Y=%L:~-1%:%P%"  
  
setx path "%%JAVA_HOME%%\bin;%Y%" -m  
echo.  
echo.  
goto end

:end
echo 请按任意键退出。
@Pause>nul