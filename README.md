# book-store

## Used Technologies

- Java 17, Gradle
- Spring (AOP, Boot, Data, Security)
- Hibernate, Liquibase, PostgresSQL, Redis
- Groovy, Spock
- Docker

## Requirements

- Docker

## Run

To run the application, run docker and:

1. Execute the following command on terminal:

   ```docker
   docker-compose up -d
   ```

&ensp;
or

2. Execute the following commands on terminal:

   ```docker
   docker-compose -f docker-compose-local.yaml up -d
   ```

   ```gradle
   ./gradlew bootRun
   ```

## Test

To run the tests, execute the following command on terminal:

   ```gradle
   ./gradlew test
   ```