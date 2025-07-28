# ERP Backend Java – Documentation

## Requirements
- Java 17+
- Maven 3.8+
- Docker and Docker Compose
- PostgreSQL 15 (already included via docker-compose)
- IDE (recommended: IntelliJ IDEA)

---

## Generate QueryDSL classes

Before building or running the application, generate the QueryDSL classes:
```bash
mvn compile
```
These classes will be generated under `target/generated-sources`. Make sure your IDE marks this folder as a **Generated Sources**


## How to run

### 1. start the database
```bash
docker-compose up -d
```

This will start a PostgreSQL container on `localhost:5432`, with:

- Database: `backend`  
- User: `postgres`  
- Password: `postgres`  

---

### 2. Run the application
You can run it from the IDE (main class `Main`) or via Maven:

```bash
mvn spring-boot:run
```

---

## API Documentation (Swagger)

Once the application is running, access:

```
http://localhost:8080/swagger-ui.html
```

or

```
http://localhost:8080/swagger-ui/index.html
```

This interface contains all endpoints for the following modules:
- **Products**
- **Orders**
- **Order Items**

---

## Automated Tests

Automated tests are already implemented for the main flows.

To run the tests:

```bash
mvn test
```

Tests use `MockMvc`, `JUnit 5`, `AssertJ` and `ObjectMapper` for JSON validation.

---

## Project Structure

```
├── adapter
│ ├── converter
│ ├── inbound
│ │ └── web
│ │ ├── dto
│ │ ├── errorhandler
│ │ ├── openapi
│ │ └── v1
│ └── outbound
│ └── persistence
│ ├── entity
│ └── repository
├── core
│ ├── domain
│ ├── port
│ └── usecase
├── infrastructure
└── Main
```

---

## Technologies used

- Spring Boot 3.5
- Spring Web, JPA e Validation
- PostgreSQL + Flyway
- JUnit + Mockito + AssertJ
- Swagger via `springdoc-openapi-ui`
- Docker + Docker Compose