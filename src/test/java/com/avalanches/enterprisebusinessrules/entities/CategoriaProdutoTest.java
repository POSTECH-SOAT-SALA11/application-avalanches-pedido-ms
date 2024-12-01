package com.avalanches.enterprisebusinessrules.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CategoriaProdutoTest {
    @Test
    void obterCategoriaProdutoFromValue() {
        var categoriaProduto = CategoriaProduto.fromValue("Lanche");

        assertEquals(categoriaProduto, CategoriaProduto.LANCHE);
    }
}
