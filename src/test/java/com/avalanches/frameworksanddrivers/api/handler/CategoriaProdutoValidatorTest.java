package com.avalanches.frameworksanddrivers.api.handler;

import com.avalanches.enterprisebusinessrules.entities.CategoriaProduto;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CategoriaProdutoValidatorTest {

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @Test
    public void validarCategoriaProdutoValida() {

        CategoriaProduto categoriaProduto = CategoriaProduto.LANCHE;
        CategoriaProdutoValidator categoriaProdutoValidator = new CategoriaProdutoValidator();
        boolean isValid = categoriaProdutoValidator.isValid(categoriaProduto, constraintValidatorContext);

        assertTrue(isValid);
    }

//    @Test
//    public void validarCategoriaProdutoInvalida() {
//
//        CategoriaProdutoValidator categoriaProdutoValidator = new CategoriaProdutoValidator();
//        boolean isValid = categoriaProdutoValidator.isValid(null, constraintValidatorContext);
//
//        assertFalse(isValid);
//    }
}
