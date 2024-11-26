package com.avalanches.interfaceadapters.controllers;

import com.avalanches.applicationbusinessrules.usecases.PedidoUseCase;
import com.avalanches.enterprisebusinessrules.entities.Pedido;
import com.avalanches.frameworksanddrivers.databases.interfaces.BancoDeDadosContextoInterface;
import com.avalanches.interfaceadapters.controllers.interfaces.PedidoControllerInterface;
import com.avalanches.interfaceadapters.gateways.PedidoGateway;
import com.avalanches.interfaceadapters.gateways.ProdutoGateway;
import com.avalanches.interfaceadapters.gateways.interfaces.PedidoGatewayInterface;
import com.avalanches.interfaceadapters.gateways.interfaces.ProdutoGatewayInterface;
import com.avalanches.interfaceadapters.presenters.JsonPresenter;
import com.avalanches.interfaceadapters.presenters.PedidoPresenter;
import com.avalanches.interfaceadapters.presenters.dtos.PedidoDto;
import com.avalanches.interfaceadapters.presenters.interfaces.JsonPresenterInterface;
import com.avalanches.interfaceadapters.presenters.interfaces.PedidoPresenterInterface;

import java.util.List;

public class PedidoController implements PedidoControllerInterface {

    @Override
    public Integer cadastrar(Pedido pedido, BancoDeDadosContextoInterface bancoDeDadosContexto) {
        JsonPresenterInterface jsonPresenter = new JsonPresenter();
        PedidoGatewayInterface pedidoGateway = new PedidoGateway(bancoDeDadosContexto, jsonPresenter);
        ProdutoGatewayInterface produtoGateway = new ProdutoGateway(bancoDeDadosContexto);
        PedidoUseCase pedidoUseCase = new PedidoUseCase();
        return pedidoUseCase.cadastrar(pedido, pedidoGateway, produtoGateway);
    }


    @Override
    public List<PedidoDto> listar(BancoDeDadosContextoInterface bancoDeDadosContexto) {
        JsonPresenterInterface jsonPresenter = new JsonPresenter();
        PedidoGatewayInterface pedidoGateway = new PedidoGateway(bancoDeDadosContexto, jsonPresenter);
        PedidoUseCase pedidoUseCase = new PedidoUseCase();
        List<Pedido> pedidos = pedidoUseCase.listar(pedidoGateway);
        PedidoPresenterInterface pedidoPresenter = new PedidoPresenter();
        return pedidoPresenter.pedidosToDtos(pedidos);
    }
}
