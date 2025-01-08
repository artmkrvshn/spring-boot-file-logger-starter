# Spring Boot File Logger Starter

This project consists of two Maven modules under a common parent, with dependency versions specified only in the parent.

#### Module 1: Spring Boot Starter Library

This module provides a Spring Boot starter that:

- Logs all public method calls of components annotated with **@Service**.
- Logs messages in the format: **{methodName}** args: **[{args}]** (e.g., saveFile args: ["order.pdf", 123, "application/pdf"]).
- Allows disabling the autoconfiguration via the Spring property libname.enabled.
- Ensures autoconfiguration does not activate if Spring AOP classes are absent.
- Provides the ability to exclude specific arguments from logging (e.g., large byte arrays or passwords).
- Sets the logger name to match the service class name.
- Includes tests for autoconfiguration.

#### Module 2: Web Application

This module utilizes the library from Module 1 to implement a Spring Boot web application with two REST endpoints:

- Save File:
    - Stores the file in a database (any database can be used).
    - Limits file size to a maximum of 1 MB.

- Get File:
    - Returns the file with the original Content-Type.
    - Sets the correct Content-Disposition header with the original file name.

The module also includes integration tests for the REST methods using @SpringBootTest with MockMvc and a test database
configured via Testcontainers.
Getting Started

## To build and run the project:

#### Clone the repository:

```
git clone https://github.com/artmkrvshn/spring-boot-file-logger-starter.git
cd spring-boot-file-logger-starter
```

#### Build the project:

```
mvn clean install
```

#### Run the web application:

```
cd assignment-spring-boot-aop-file-storage
mvn spring-boot:run
```

### Configuration

To disable the autoconfiguration of the logging library, set the following property in your application.properties or
**application.properties**:

`logging.enabled=true`

To exclude specific method arguments from being logged, configure the appropriate settings in your application
configuration files.
Testing

The project includes unit and integration tests. To run the tests:

```
mvn test
```