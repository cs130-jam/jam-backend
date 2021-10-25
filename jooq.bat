@echo off

call mvn help:evaluate -Dexpression="settings.localRepository" -q -Doutput="tmp_mvn_repo_path_file.txt"

for /f "delims=" %%x in (tmp_mvn_repo_path_file.txt) do set RESULT=%%x
set CLASSPATH=%RESULT%\org\reactivestreams\reactive-streams\1.0.3\reactive-streams-1.0.3.jar;%RESULT%\mysql\mysql-connector-java\8.0.26\mysql-connector-java-8.0.26.jar;%RESULT%\org\jooq\jooq\3.14.15\jooq-3.14.15.jar;%RESULT%\org\jooq\jooq-meta\3.14.15\jooq-meta-3.14.15.jar;%RESULT%\org\jooq\jooq-codegen\3.14.15\jooq-codegen-3.14.15.jar;%RESULT%\javax\xml\bind\jaxb-api\2.3.1\jaxb-api-2.3.1.jar
echo %CLASSPATH%

java org.jooq.codegen.GenerationTool jooq-codegen.xml

del tmp_mvn_repo_path_file.txt