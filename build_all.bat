cd config-server
call gradlew clean build
cd ..\discovery-server
call gradlew clean build
cd ..\gateway
call gradlew clean build
cd ..