package com.avalanches.frameworksanddrivers.api.handler;

import com.avalanches.enterprisebusinessrules.entities.CategoriaProduto;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintValidatorContext;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class CategoriaProdutoValidatorTest {

    @Inject
    private ConstraintValidatorContext constraintValidatorContext;

    @Spy
    @InjectMocks
    private CategoriaProdutoValidator categoriaProdutoValidator;

    @BeforeEach
    void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
        categoriaProdutoValidator = new CategoriaProdutoValidator();
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    AutoCloseable openMocks;

    @Test
    public void validarCategoriaProduto_Success() {

        CategoriaProduto categoriaProduto = CategoriaProduto.LANCHE;

        boolean isValid = categoriaProdutoValidator.isValid(categoriaProduto, constraintValidatorContext);

        assertTrue(isValid);
    }

//    @Test
//    public void validarCategoriaProduto_Error() {
//
//        CategoriaProdutoValidator categoriaProdutoValidator = new CategoriaProdutoValidator();
//
//        boolean isValid = categoriaProdutoValidator.isValid(null, constraintValidatorContext);
//
//        assertFalse(isValid);
//    }
}
