@ECHO OFF
SET DIR=%~dp0
SET CLASSPATH=%DIR%\gradle\wrapper\gradle-wrapper.jar
IF NOT EXIST "%CLASSPATH%" (
  ECHO Missing gradle-wrapper.jar. Run gradle wrapper once to generate it.
  EXIT /B 1
)
java -Dorg.gradle.appname=gradlew -classpath "%CLASSPATH%" org.gradle.wrapper.GradleWrapperMain %*
