package com.avalanches.applicationbusinessrules.usecases;

import com.avalanches.applicationbusinessrules.usecases.interfaces.PedidoUseCaseInterface;
import com.avalanches.enterprisebusinessrules.entities.Pedido;
import com.avalanches.enterprisebusinessrules.entities.PedidoProduto;
import com.avalanches.interfaceadapters.gateways.interfaces.PedidoGatewayInterface;
import com.avalanches.interfaceadapters.gateways.interfaces.ProdutoGatewayInterface;
import org.webjars.NotFoundException;

import java.math.BigDecimal;
import java.util.List;

public class PedidoUseCase implements PedidoUseCaseInterface {

    @Override
    public Integer cadastrar(Pedido pedido,
                             PedidoGatewayInterface pedidoGateway,
                             ProdutoGatewayInterface produtoGateway
                             ) {

        for(PedidoProduto p: pedido.getListaProduto())
            if(!produtoGateway.verificaProdutoExiste(p.getIdProduto())) {
                throw new NotFoundException("O produto " + p.getIdProduto() + " não foi encontrado.");
            }

        pedido.setValor(this.calcularValorTotal(pedido, produtoGateway));

        pedidoGateway.cadastrar(pedido);
//        PagamentoUseCase pagamentoUseCase = new PagamentoUseCase();
//        pagamentoUseCase.efetuarPagamento(pedido.getId(), pagamentoGateway);

        for(PedidoProduto p: pedido.getListaProduto())
            pedidoGateway.cadastrarProdutosPorPedido(pedido.getId(), p);

        return pedido.getId();
    }

    @Override
    public List<Pedido> listar(PedidoGatewayInterface pedidoGateway) {
        return pedidoGateway.listar();
    }


    private BigDecimal calcularValorTotal(Pedido pedido, ProdutoGatewayInterface produtoGateway) {
        BigDecimal valorTotal = BigDecimal.ZERO;
        for(PedidoProduto p: pedido.getListaProduto()){
            if(!produtoGateway.verificaProdutoExiste(p.getIdProduto())) {
                throw new NotFoundException("O produto " + p.getIdProduto() + " não foi encontrado.");
            }
            BigDecimal valorProduto = p.getValorUnitario().multiply(BigDecimal.valueOf(p.getQuantidade()));
            valorTotal = valorTotal.add(valorProduto);
        }
        return valorTotal;
    }
}
