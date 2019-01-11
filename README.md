README first:

This is a Server application for the St. Pauli service App 
H2-InMemoryDB in there. Mysql not supported yet.
Request-Reply via REST

JDK 10 needed!!!

Tested with jdk 10, windows 10 and IntelliJ Ultimate Edition!

++++++++++++++++++++++++++++++++++++++++++++

to start this application(windows):
gradlew clean bootRun -Pprofile=local -Pport=8080 (or Port 443)
or
gradlew bootRun

to build this application(windows):
gradlew clean build

call SwaggerApi
http://localhost:443/swagger-ui.html#/

call h2-Database:
http://localhost:443/h2-console
