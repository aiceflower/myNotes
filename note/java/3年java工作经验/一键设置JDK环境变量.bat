@echo off
color 2e
echo.-----------------------------------------------------------
echo.��ѡ��ʹ��JDK�İ汾��
echo.
echo. 1.ʹ��JDK1.6��������������1��
echo.
echo. 2.ʹ��JDK1.7��������������2��
echo.
echo. 3.ʹ��JDK1.8��������������3��
echo.-----------------------------------------------------------

set /p version=���������ֲ����س���ȷ��:
if %version% == 1 goto jdk6
if %version% == 2 goto jdk7
if %version% == 3 goto jdk8
cls
set choice=
echo ��������������!
goto end

::���û�������Ϊjdk1.6
:jdk6
set javahome=C:\Users\pillow\Desktop\grg\jdk\jdk1.6.0_43
set jdkVersion=1.6
goto set env

::���û�������Ϊjdk1.7
:jdk7
set javahome=C:\Users\pillow\Desktop\grg\jdk\jdk7\jdk1.7.0_80
set jdkVersion=1.7
goto set env

::���û�������Ϊjdk1.9
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
echo *                   JDK ϵͳ������������                   *  
echo *                                                          *  
echo ************************************************************  
echo.  
echo === ׼�����û�������: JAVA_HOME=%javahome%  
echo === ע��: ���JAVA_HOME����,�ᱻ����,�˲����������,����ϸ���ȷ��!! ===  
echo.  
echo === ׼�����û�������(�����и�.): classPath=%%JAVA_HOME%%\lib\tools.jar;%%JAVA_HOME%%\lib\dt.jar;.  
echo === ע��: ���classPath����,�ᱻ����,�˲����������,����ϸ���ȷ��!! ===  
echo.  
echo === ׼�����û�������: PATH=%%JAVA_HOME%%\bin  
echo === ע��: PATH��׷������ǰ��,  
echo.  
set /P EN=��ȷ�Ϻ� �س��� ��ʼ����!  
echo.  
echo.  ����JAVA��������Ϊ%jdkVersion%��ʼ:
echo === �´����������� JAVA_HOME=%javahome%  
setx "JAVA_HOME" "%javahome%" -M  
echo.  
echo === �´����������� classPath=%%JAVA_HOME%%\lib\tools.jar;%%JAVA_HOME%%%\lib\dt.jar;.  
setx "classPath" "%%JAVA_HOME%%\lib\tools.jar;%%JAVA_HOME%%%\lib\dt.jar;." -m  
echo.  
echo === ��׷�ӻ�������(׷�ӵ���ǰ��) PATH=%%JAVA_HOME%%\bin  
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
echo �밴������˳���
@Pause>nul