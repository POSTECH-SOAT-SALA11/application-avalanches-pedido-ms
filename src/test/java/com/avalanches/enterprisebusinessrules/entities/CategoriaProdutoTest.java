package com.avalanches.enterprisebusinessrules.entities;

import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CategoriaProdutoTest {
    @Test
    public void obterCategoriaProdutoFromValue() {
        var categoriaProduto = CategoriaProduto.fromValue("Lanche");

        assertEquals(categoriaProduto, CategoriaProduto.LANCHE);
    }
}
