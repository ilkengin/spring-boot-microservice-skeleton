FROM openjdk:14-alpine
ADD build/libs/gateway-*.jar app.jar
ADD run.sh run.sh
RUN dos2unix run.sh
RUN chmod +x run.sh
CMD ["./run.sh"]
EXPOSE 9999
