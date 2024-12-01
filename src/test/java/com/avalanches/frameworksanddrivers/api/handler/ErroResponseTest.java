package com.avalanches.frameworksanddrivers.api.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class ErroResponseTest {

    private ErroResponse erroResponse;

    @BeforeEach
    void setUp() {
        // Initialize the object before each test
        LocalDateTime timestamp = LocalDateTime.now();
        erroResponse = new ErroResponse(400, "Bad Request", timestamp);
    }

    @Test
    void testConstructor_InitializesFieldsCorrectly() {
        // Arrange
        int status = 400;
        String mensagem = "Bad Request";
        LocalDateTime timestamp = LocalDateTime.now();

        // Act
        ErroResponse erroResponse = new ErroResponse(status, mensagem, timestamp);

        // Assert
        assertEquals(status, erroResponse.getStatus());
        assertEquals(mensagem, erroResponse.getmensagem());
        assertEquals(timestamp.format(DateTimeFormatter.ISO_DATE_TIME), erroResponse.getTimestamp());
    }

    @Test
    void testGetters_ReturnCorrectValues() {
        // Assert
        assertEquals(400, erroResponse.getStatus());
        assertEquals("Bad Request", erroResponse.getmensagem());
        assertNotNull(erroResponse.getTimestamp());
        assertTrue(erroResponse.getTimestamp().matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.*"));
    }

    @Test
    void testSetters_UpdateValuesCorrectly() {
        // Act
        erroResponse.setStatus(500);
        erroResponse.setmensagem("Internal Server Error");
        erroResponse.setTimestamp("2024-01-01T10:00:00");

        // Assert
        assertEquals(500, erroResponse.getStatus());
        assertEquals("Internal Server Error", erroResponse.getmensagem());
        assertEquals("2024-01-01T10:00:00", erroResponse.getTimestamp());
    }

    @Test
    void testTimestampFormatting_UsesISODateTimeFormat() {
        // Arrange
        LocalDateTime timestamp = LocalDateTime.of(2024, 11, 27, 15, 30, 45);

        // Act
        ErroResponse erroResponse = new ErroResponse(400, "Bad Request", timestamp);

        // Assert
        assertEquals("2024-11-27T15:30:45", erroResponse.getTimestamp());
    }
}
