package com.avalanches.frameworksanddrivers.databases.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostgresConfigTest {

    private PostgresConfig postgresConfig;

    @Mock
    private Environment mockEnvironment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        postgresConfig = new PostgresConfig();
        postgresConfig.ambiente = mockEnvironment; // Inject the mocked environment
    }

    @Test
    void testOrigemDados() {
        // Arrange
        when(mockEnvironment.getProperty("spring.datasource.driver-class-name")).thenReturn("org.postgresql.Driver");
        when(mockEnvironment.getProperty("spring.datasource.url")).thenReturn("jdbc:postgresql://localhost:5432/mydb");
        when(mockEnvironment.getProperty("spring.datasource.username")).thenReturn("myuser");
        when(mockEnvironment.getProperty("spring.datasource.password")).thenReturn("mypassword");

        // Act
        DataSource dataSource = postgresConfig.OrigemDados();

        // Assert
        assertNotNull(dataSource, "DataSource should not be null");
        assertTrue(dataSource instanceof DriverManagerDataSource, "DataSource should be an instance of DriverManagerDataSource");

        DriverManagerDataSource driverManagerDataSource = (DriverManagerDataSource) dataSource;
        assertEquals("jdbc:postgresql://localhost:5432/mydb", driverManagerDataSource.getUrl());
        assertEquals("myuser", driverManagerDataSource.getUsername());
        assertEquals("mypassword", driverManagerDataSource.getPassword());

        verify(mockEnvironment).getProperty("spring.datasource.url");
        verify(mockEnvironment).getProperty("spring.datasource.username");
        verify(mockEnvironment).getProperty("spring.datasource.password");
    }

    @Test
    void testJdbcTemplate() {
        // Arrange
        DataSource mockDataSource = mock(DataSource.class);

        // Act
        JdbcTemplate jdbcTemplate = postgresConfig.jdbcTemplate(mockDataSource);

        // Assert
        assertNotNull(jdbcTemplate, "JdbcTemplate should not be null");
        assertEquals(mockDataSource, jdbcTemplate.getDataSource(), "DataSource should match the provided DataSource");
    }
}
