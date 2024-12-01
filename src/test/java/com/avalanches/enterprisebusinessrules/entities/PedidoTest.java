package com.avalanches.enterprisebusinessrules.entities;

import jakarta.validation.constraints.AssertTrue;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class PedidoTest {


    @Test
    void testGetPedido() {
        Pedido pedido = new Pedido(1,
                StatusPedido.EMPREPARACAO,
                new BigDecimal("10.0"),
                LocalDateTime.now(),
                LocalDateTime.now(),
                1);

        assertThat(pedido.getId()).isEqualTo(1);

    }

}
