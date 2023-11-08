FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY build/libs/book-store-0.0.1.jar app.jar
ENTRYPOINT ["java"]
CMD ["-jar", "app.jar"]
EXPOSE 8888