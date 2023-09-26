FROM alpine/git as clone
MAINTAINER geetatech5@gmail.com
WORKDIR /app
RUN git clone https://github.com/geetasaluja/wallet_code.git

FROM maven:3.6.1-jdk-8-alpine as build
WORKDIR /app
COPY --from=clone /app/wallet_code /app
RUN mvn -f /app/walletapi clean install -DskipTests

FROM openjdk:8-jre-alpine
WORKDIR /app
COPY --from=build /app/walletapi/target/wallet-microservice.jar /app/wallet-microservice.jar
EXPOSE 8080:8080
CMD ["java", "-jar", "/app/wallet-microservice.jar"]




