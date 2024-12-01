package com.avalanches.frameworksanddrivers.api.handler;

import com.avalanches.enterprisebusinessrules.entities.StatusPedido;
import com.avalanches.frameworksanddrivers.databases.StatusPedidoInvalidoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.webjars.NotFoundException;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void testHandleValidationExceptions_ReturnsValidationErrorDetails() {
        // Arrange
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("objectName", "fieldName", "must not be null");
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);

        // Act
        ResponseEntity<List<ValidationErrorDetails>> response = globalExceptionHandler.handleValidationExceptions(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("fieldName", response.getBody().get(0).getField());
        assertEquals("must not be null", response.getBody().get(0).getMessage());
    }

    @Test
    void testHandlePedidoInvalidoException_ReturnsPedidoInvalidoErrorResponse() {
        // Arrange
        StatusPedidoInvalidoException exception = new StatusPedidoInvalidoException(StatusPedido.PRONTO);

        // Act
        ResponseEntity<ErroResponse> response = globalExceptionHandler.handleStatusPedidoInvalidoException(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getStatus());
        assertEquals("A atualização para o status PRONTO é inválida", response.getBody().getmensagem());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void testHandleNotFoundException_ReturnsNotFoundErrorResponse() {
        // Arrange
        NotFoundException exception = new NotFoundException("Resource not found");

        // Act
        ResponseEntity<ErroResponse> response = globalExceptionHandler.handleNotFoundException(exception);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().getStatus());
        assertEquals("Resource not found", response.getBody().getmensagem());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void testHandleHttpMessageNotReadableException_ReturnsBadRequestErrorResponse() {
        // Arrange
        Throwable cause = new Throwable("Invalid JSON payload");
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException("JSON parsing error", cause);

        // Act
        ResponseEntity<ErroResponse> response = globalExceptionHandler.handleHttpMessageNotReadableException(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getStatus());
        assertEquals("Invalid JSON payload", response.getBody().getmensagem());
        assertNotNull(response.getBody().getTimestamp());
    }

}
