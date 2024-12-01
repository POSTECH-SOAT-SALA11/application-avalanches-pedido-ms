package com.avalanches.frameworksanddrivers.databases.config;

import com.avalanches.frameworksanddrivers.databases.config.RedisConfig;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RedisConfigTest {

    private RedisConfig redisConfig;

    @Mock
    private Environment environment;

    @Mock
    private RedisClient mockRedisClient;

    @Mock
    private StatefulRedisConnection<String, String> mockConnection;

    @Mock
    private RedisCommands<String, String> mockRedisCommands;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        redisConfig = new RedisConfig();
        redisConfig.ambiente = environment;
    }

    @Test
    void testRedisClient_NoPassword() {
        // Arrange
        when(environment.getProperty("spring.data.redis.host")).thenReturn("localhost");
        when(environment.getProperty("spring.data.redis.port")).thenReturn("6379");
        when(environment.getProperty("spring.data.redis.password")).thenReturn(null);

        // Act
        RedisClient redisClient = redisConfig.redisClient();

        // Assert
        assertNotNull(redisClient, "RedisClient should not be null");
        verify(environment).getProperty("spring.data.redis.host");
        verify(environment).getProperty("spring.data.redis.port");
        verify(environment).getProperty("spring.data.redis.password");
    }

    @Test
    void testRedisClient_WithPassword() {
        // Arrange
        when(environment.getProperty("spring.data.redis.host")).thenReturn("localhost");
        when(environment.getProperty("spring.data.redis.port")).thenReturn("6379");
        when(environment.getProperty("spring.data.redis.password")).thenReturn("mypassword");

        // Act
        RedisClient redisClient = redisConfig.redisClient();

        // Assert
        assertNotNull(redisClient, "RedisClient should not be null");
        verify(environment).getProperty("spring.data.redis.host");
        verify(environment).getProperty("spring.data.redis.port");
        verify(environment).getProperty("spring.data.redis.password");
    }

    @Test
    void testRedisConnection() {
        // Arrange
        when(mockRedisClient.connect()).thenReturn(mockConnection);

        // Act
        StatefulRedisConnection<String, String> connection = redisConfig.redisConnection(mockRedisClient);

        // Assert
        assertNotNull(connection, "RedisConnection should not be null");
        verify(mockRedisClient).connect();
    }

    @Test
    void testRedisCommands() {
        // Arrange
        when(mockConnection.sync()).thenReturn(mockRedisCommands);

        // Act
        RedisCommands<String, String> redisCommands = redisConfig.redisCommands(mockConnection);

        // Assert
        assertNotNull(redisCommands, "RedisCommands should not be null");
        verify(mockConnection).sync();
    }
}
