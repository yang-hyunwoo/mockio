package com.mockio.user_service;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

@DataJpaTest(properties = "jpa.auditing.enabled=true")
@Testcontainers
@AutoConfigureTestDatabase(replace = Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public abstract class PostgresDataJpaTest {

    @Container
    @org.springframework.boot.testcontainers.service.connection.ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

//    @DynamicPropertySource
//    static void overrideProps(DynamicPropertyRegistry r) {
//        r.add("spring.datasource.url", postgres::getJdbcUrl);
//        r.add("spring.datasource.username", postgres::getUsername);
//        r.add("spring.datasource.password", postgres::getPassword);
//    }

//    @Container
//    @org.springframework.boot.testcontainers.service.connection.ServiceConnection
//    static PostgreSQLContainer<?> postgres =
//            new PostgreSQLContainer<>("postgres:16-alpine")
//                    .withDatabaseName("test")
//                    .withUsername("test")
//                    .withPassword("test");
//
//    @DynamicPropertySource
//    static void props(DynamicPropertyRegistry registry) {
//        registry.add("spring.flyway.enabled", () -> true);
//        registry.add("spring.flyway.locations", () -> "classpath:db/migration");
//        registry.add("spring.jpa.hibernate.ddl-auto", () -> "validate");
//        registry.add("logging.level.org.flywaydb", () -> "DEBUG");
//        registry.add("logging.level.org.flywaydb", () -> "INFO");
//        registry.add("logging.level.org.springframework.jdbc.datasource", () -> "INFO");
//    }
}