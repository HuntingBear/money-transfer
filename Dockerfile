FROM adoptopenjdk/openjdk11:alpine-jre
COPY ./target/money-transfer-1.0.jar /project/money-transfer.jar
ENTRYPOINT ["java", "-jar", "/project/money-transfer.jar"]

