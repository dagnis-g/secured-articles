# secured-articles

REST API using Java, Spring Boot, Spring Security, H2, Liquibase, Swagger

### How to run

To run Spring Boot application. In project root directory

```shell

mvn spring-boot:run 

```

### Endpoints

```
`http://localhost:8080articles/save` - ADMIN, USER
`http://localhost:8080articles/{page}` - ADMIN, USER
`http://localhost:8080articles/statistics` - ADMIN
```

### H2

To access H2 database navigate to

```
Browser: http://localhost:8080/h2-console/

Driver Class: org.h2.Driver
JDBC URL: jdbc:h2:mem:testdb
User Name: sa
Password: 

On startup Liquibase insert two users in database
1. username - admin, password - admin
2. username - user, password - user

```

### Swagger

```
Browser:  http://localhost:8080/swagger-ui/index.html

Access by loging in as one of the users
```

# secured-articles# secured-articles
