package com.avalanches.frameworksanddrivers.databases;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JsonProcessingCustomExceptionTest {

    @Test
    void testJsonProcessingCustomExceptionMessage() {
        // Arrange
        String errorMessage = "Invalid JSON format";

        // Act
        JsonProcessingCustomException exception = new JsonProcessingCustomException(errorMessage);

        // Assert
        assertEquals("Erro ao processar JSON: " + errorMessage, exception.getMessage());
    }

    @Test
    void testJsonProcessingCustomExceptionType() {
        // Arrange
        String errorMessage = "Invalid JSON format";

        // Act
        JsonProcessingCustomException exception = new JsonProcessingCustomException(errorMessage);

        // Assert
        assertTrue(exception instanceof JsonProcessingCustomException);
    }

    @Test
    void testJsonProcessingCustomExceptionIsRuntimeException() {
        // Arrange
        String errorMessage = "Invalid JSON format";

        // Act
        JsonProcessingCustomException exception = new JsonProcessingCustomException(errorMessage);

        // Assert
        assertTrue(exception instanceof RuntimeException);
    }
}
