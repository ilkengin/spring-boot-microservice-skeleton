FROM openjdk:14-alpine
ADD build/libs/discovery-server-*.jar app.jar
ADD run.sh run.sh
RUN dos2unix run.sh
RUN chmod +x run.sh
CMD ["./run.sh"]
EXPOSE 8761
