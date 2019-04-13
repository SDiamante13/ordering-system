package com.diamante.orderingsystem;

import org.junit.ClassRule;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.Duration;

@ContextConfiguration(initializers = { TestDatabaseSetup.Initializer.class })
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public abstract class TestDatabaseSetup {

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = (PostgreSQLContainer) new PostgreSQLContainer("postgres:alpine")
            .withDatabaseName("testDb")
            .withUsername("postgresTest")
            .withPassword("password")
            .withStartupTimeout(Duration.ofSeconds(600));

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                    "spring.datasource.password=" + postgreSQLContainer.getPassword(),
                    "spring.jpa.hibernate.ddl-auto=" + "update"
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }
}
