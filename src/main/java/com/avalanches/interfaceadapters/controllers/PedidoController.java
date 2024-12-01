package com.avalanches.interfaceadapters.controllers;

import com.avalanches.applicationbusinessrules.usecases.PedidoUseCase;
import com.avalanches.enterprisebusinessrules.entities.Pedido;
import com.avalanches.enterprisebusinessrules.entities.StatusPedido;
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
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.util.List;

public class PedidoController implements PedidoControllerInterface {

    @Override
    public Integer cadastrar(Pedido pedido, BancoDeDadosContextoInterface bancoDeDadosContexto) {
        JsonPresenterInterface jsonPresenter = new JsonPresenter();
        PedidoGatewayInterface pedidoGateway = criarPedidoGateway(bancoDeDadosContexto, jsonPresenter);
        ProdutoGatewayInterface produtoGateway = criarProdutoGateway(bancoDeDadosContexto);
        PedidoUseCase pedidoUseCase = criarPedidoUseCase();
        return pedidoUseCase.cadastrar(pedido, pedidoGateway, produtoGateway);
    }

    @Override
    public void atualizaStatus(Integer idPedido, StatusPedido statusPedido, BancoDeDadosContextoInterface bancoDeDadosContexto) {
        JsonPresenterInterface jsonPresenter = new JsonPresenter();
        PedidoGatewayInterface pedidoGateway = criarPedidoGateway(bancoDeDadosContexto, jsonPresenter);
        PedidoUseCase pedidoUseCase = criarPedidoUseCase();
        pedidoUseCase.atualizaStatus(idPedido, statusPedido, pedidoGateway);
    }

    @Override
    public List<PedidoDto> listar(BancoDeDadosContextoInterface bancoDeDadosContexto) {
        JsonPresenterInterface jsonPresenter = new JsonPresenter();
        PedidoGatewayInterface pedidoGateway = criarPedidoGateway(bancoDeDadosContexto, jsonPresenter);
        PedidoUseCase pedidoUseCase = criarPedidoUseCase();
        List<Pedido> pedidos = pedidoUseCase.listar(pedidoGateway);
        PedidoPresenterInterface pedidoPresenter = new PedidoPresenter();
        return pedidoPresenter.pedidosToDtos(pedidos);
    }

    protected ProdutoGateway criarProdutoGateway(BancoDeDadosContextoInterface bancoDeDadosContexto) {
        return new ProdutoGateway(bancoDeDadosContexto, new GeneratedKeyHolder());
    }

    protected PedidoGateway criarPedidoGateway(BancoDeDadosContextoInterface bancoDeDadosContexto, JsonPresenterInterface jsonPresenter) {
        return new PedidoGateway(bancoDeDadosContexto, jsonPresenter);
    }

    protected PedidoUseCase criarPedidoUseCase() {
        return new PedidoUseCase();
    }
}
