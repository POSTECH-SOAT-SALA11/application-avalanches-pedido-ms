package com.avalanches.frameworksanddrivers.api.handler;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ValidationErrorDetailsTest {

    @Test
    void testConstructorAndGetters() {
        // Arrange
        String expectedField = "username";
        String expectedMessage = "Username is required";

        // Act
        ValidationErrorDetails details = new ValidationErrorDetails(expectedField, expectedMessage);

        // Assert
        assertEquals(expectedField, details.getField(), "Field value should match the expected value");
        assertEquals(expectedMessage, details.getMessage(), "Message value should match the expected value");
    }

    @Test
    void testSetField() {
        // Arrange
        ValidationErrorDetails details = new ValidationErrorDetails("username", "Username is required");
        String newField = "email";

        // Act
        details.setField(newField);

        // Assert
        assertEquals(newField, details.getField(), "Field should be updated to the new value");
    }

    @Test
    void testSetMessage() {
        // Arrange
        ValidationErrorDetails details = new ValidationErrorDetails("username", "Username is required");
        String newMessage = "Email is invalid";

        // Act
        details.setMessage(newMessage);

        // Assert
        assertEquals(newMessage, details.getMessage(), "Message should be updated to the new value");
    }
}
