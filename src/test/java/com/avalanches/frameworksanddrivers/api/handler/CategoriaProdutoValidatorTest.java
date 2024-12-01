package com.avalanches.frameworksanddrivers.api.handler;

import com.avalanches.enterprisebusinessrules.entities.CategoriaProduto;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CategoriaProdutoValidatorTest {

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    AutoCloseable openMocks;

    @Test
    public void validarCategoriaProduto_Success() {

        CategoriaProduto categoriaProduto = CategoriaProduto.LANCHE;
        CategoriaProdutoValidator categoriaProdutoValidator = new CategoriaProdutoValidator();
        boolean isValid = categoriaProdutoValidator.isValid(categoriaProduto, constraintValidatorContext);

        assertTrue(isValid);
    }

//    @Test
//    public void validarCategoriaProduto_Error() {
//
//
//        CategoriaProdutoValidator categoriaProdutoValidator = new CategoriaProdutoValidator();
//        boolean isValid = categoriaProdutoValidator.isValid(null, constraintValidatorContext);
//
//        assertFalse(isValid);
//    }
}
