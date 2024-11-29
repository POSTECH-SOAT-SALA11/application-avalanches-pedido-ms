package com.avalanches.repository;

import com.avalanches.enterprisebusinessrules.entities.Pedido;
import com.avalanches.enterprisebusinessrules.entities.PedidoProduto;
import com.avalanches.enterprisebusinessrules.entities.StatusPedido;
import com.avalanches.interfaceadapters.gateways.interfaces.PedidoGatewayInterface;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PedidoGatewayTest {

    @Mock
    private PedidoGatewayInterface pedidoGateway;


    AutoCloseable openMocks;

    @BeforeEach
    void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void deveRegistrarPedido(){
        //Arrange

        var pedido = getPedido();

        //Act
        doNothing().when(pedidoGateway).cadastrar(any(Pedido.class));

        pedidoGateway.cadastrar(pedido);

        //Assert
        verify(pedidoGateway, times(1)).cadastrar(any(Pedido.class));
    }

    @Test
    void deveListar(){
        //Arrange
        var listaPedidos = new ArrayList<Pedido>();
        var pedido1 = getPedido();
        var pedido2 = getPedido();
        listaPedidos.add(pedido1);
        listaPedidos.add(pedido2);

        //Act

        when(pedidoGateway.listar()).thenReturn(listaPedidos);

        var pedidos = pedidoGateway.listar();

        //Assert
        assertThat(pedidos).hasSizeGreaterThan(0);
    }

    @Test
    void deveCadastrarProdutosPorPedido()
    {
        //Arrange

        var pedido1 = getPedido();

        //Act

        doNothing().when(pedidoGateway).cadastrarProdutosPorPedido(anyInt(), any(PedidoProduto.class));

        for(PedidoProduto p: pedido1.getListaProduto())
            pedidoGateway.cadastrarProdutosPorPedido(pedido1.getId(), p);

        //Assert
        verify(pedidoGateway, times(pedido1.getListaProduto().size())).cadastrarProdutosPorPedido(anyInt(), any(PedidoProduto.class));
    }

    @Test
    void deveAtualizarStatus(){
        //Arrange
        doNothing().when(pedidoGateway).atualizaStatus(anyInt(),any(StatusPedido.class));
        //Act

        pedidoGateway.atualizaStatus(1, StatusPedido.PRONTO);

        //Assert
        verify(pedidoGateway, times(1)).atualizaStatus(anyInt(),any(StatusPedido.class));
    }

    @Test
    void deveVerificarPedidoExiste(){

        //Act
        when(pedidoGateway.verificaPedidoExiste(anyInt())).thenReturn(true);

        boolean pedidoExiste = pedidoGateway.verificaPedidoExiste(1);

        //Assert

        assertThat(pedidoExiste).isTrue();
    }

    @Test
    void deveBuscarStatusPedido()
    {
        int idPedido = 1;

        //Act
        when(pedidoGateway.buscarStatusPedido(anyInt())).thenReturn("Em Preparação");

        String statusPedido = pedidoGateway.buscarStatusPedido(idPedido);

        //Assert

        assertThat(statusPedido).isEqualTo("Em Preparação");
    }




    private static @NotNull Pedido getPedido() {
        var listaProdutos = new ArrayList<PedidoProduto>();
        var pedidoProduto1 = new PedidoProduto(1,3,new BigDecimal("10"));
        var pedidoProduto2 = new PedidoProduto(2,1,new BigDecimal("20"));

        listaProdutos.add(pedidoProduto1);
        listaProdutos.add(pedidoProduto2);

        return new Pedido(1,
                StatusPedido.RECEBIDO,
                new BigDecimal("100.00"),
                LocalDateTime.now(),
                LocalDateTime.now(),
                listaProdutos,
                10
        );
    }


}
