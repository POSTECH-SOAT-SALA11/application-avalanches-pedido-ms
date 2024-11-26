package com.avalanches.applicationbusinessrules.usecases.interfaces;

import com.avalanches.enterprisebusinessrules.entities.Pedido;
import com.avalanches.interfaceadapters.gateways.interfaces.PedidoGatewayInterface;
import com.avalanches.interfaceadapters.gateways.interfaces.ProdutoGatewayInterface;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PedidoUseCaseInterface {
    Integer cadastrar(Pedido pedido, PedidoGatewayInterface pedidoGateway, ProdutoGatewayInterface produtoGateway);
    List<Pedido> listar(PedidoGatewayInterface pedidoGateway);

}
