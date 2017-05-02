rem @echo off

SETLOCAL enabledelayedexpansion
TITLE Elasticsearch 2.4.0

SET params='%*'

:loop
FOR /F "usebackq tokens=1* delims= " %%A IN (!params!) DO (
    SET current=%%A
    SET params='%%B'
	SET silent=N
	
	IF "!current!" == "-s" (
		SET silent=Y
	)
	IF "!current!" == "--silent" (
		SET silent=Y
	)	
	
	IF "!silent!" == "Y" (
		SET nopauseonerror=Y
	) ELSE (
	    IF "x!newparams!" NEQ "x" (
	        SET newparams=!newparams! !current!
        ) ELSE (
            SET newparams=!current!
        )
	)
	
    IF "x!params!" NEQ "x" (
		GOTO loop
	)
)

SET HOSTNAME=%COMPUTERNAME%

CALL "%~dp0elasticsearch.in.bat"
IF ERRORLEVEL 1 (
	IF NOT DEFINED nopauseonerror (
		PAUSE
	)
	EXIT /B %ERRORLEVEL%
)

rem 增加调试参数
"%JAVA_HOME%\bin\java" %JAVA_OPTS% -javaagent:D:\600.self\05.code\04.java\TProfiler\tprofiler-0.2.jar -Xdebug -Xrunjdwp:transport=dt_socket,address=8500,server=y,suspend=y  %ES_JAVA_OPTS% %ES_PARAMS% -cp "%ES_CLASSPATH%" "org.elasticsearch.bootstrap.Elasticsearch" start !newparams!

rem "%JAVA_HOME%\bin\java" %JAVA_OPTS% -javaagent:D:\600.self\05.code\04.java\TProfiler\tprofiler-0.2.jar  -Dorg.simonme.tracer.enableinit=true  %ES_JAVA_OPTS% %ES_PARAMS% -cp "%ES_CLASSPATH%" "org.elasticsearch.bootstrap.Elasticsearch" start !newparams!

rem "%JAVA_HOME%\bin\java" %JAVA_OPTS% %ES_JAVA_OPTS% %ES_PARAMS% -cp "%ES_CLASSPATH%" "org.elasticsearch.bootstrap.Elasticsearch" start !newparams!


ENDLOCAL
