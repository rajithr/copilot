# Credit Risk Spring Boot example

This repository contains a minimal Spring Boot application that exposes a pageable REST GET API to fetch credit risk records.

Run:

1. mvn clean package
2. mvn spring-boot:run

API:

- GET /api/credit-risks?page=0&size=10&sort=score,desc

H2 Console: http://localhost:8080/h2-console (JDBC URL: jdbc:h2:mem:creditdb)
