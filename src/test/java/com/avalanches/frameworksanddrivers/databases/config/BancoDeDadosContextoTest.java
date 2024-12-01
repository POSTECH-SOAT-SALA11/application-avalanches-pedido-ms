package com.avalanches.frameworksanddrivers.databases.config;

import io.lettuce.core.api.sync.RedisCommands;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BancoDeDadosContextoTest {

    @Mock
    private JdbcTemplate mockJdbcTemplate;

    @Mock
    private RedisCommands<String, String> mockRedisCommands;

    private BancoDeDadosContexto bancoDeDadosContexto;

    @BeforeEach
    void setUp() {
        // Initialize mocks and BancoDeDadosContexto
        MockitoAnnotations.openMocks(this);
        bancoDeDadosContexto = new BancoDeDadosContexto(mockJdbcTemplate, mockRedisCommands);
    }

    @Test
    void testGetJdbcTemplate() {
        // Act
        JdbcTemplate result = bancoDeDadosContexto.getJdbcTemplate();

        // Assert
        assertEquals(mockJdbcTemplate, result, "The returned JdbcTemplate should match the injected instance");
    }

    @Test
    void testGetRedisCommands() {
        // Act
        RedisCommands<String, String> result = bancoDeDadosContexto.getRedisCommands();

        // Assert
        assertEquals(mockRedisCommands, result, "The returned RedisCommands should match the injected instance");
    }
}
