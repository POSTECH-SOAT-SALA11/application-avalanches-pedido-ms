package com.avalanches.frameworksanddrivers.api;

import com.avalanches.enterprisebusinessrules.entities.*;
import com.avalanches.frameworksanddrivers.api.dto.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Convert {

    public static Pedido pedidoParamsToPedido(PedidoParams pedido) {
        return new Pedido(
            null,
            StatusPedido.RECEBIDO,
            pedido.valor(),
            pedido.dataCriacao(),
            pedido.dataFinalizacao(),
            pedido.listaProduto(),
            pedido.IdCliente()
        );
    }

}