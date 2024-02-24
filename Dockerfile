FROM openjdk:17-ea-jdk-oracle

WORKDIR /app

COPY .env .
COPY build/libs/UdInfopark-0.0.1-SNAPSHOT.jar /app

CMD ["java", "-jar", "UdInfopark-0.0.1-SNAPSHOT.jar"]