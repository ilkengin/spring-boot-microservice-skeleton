FROM openjdk:11-jre-slim
RUN apt-get update
RUN apt-get install wget -y
ADD build/libs/config-server-*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
EXPOSE 8888
