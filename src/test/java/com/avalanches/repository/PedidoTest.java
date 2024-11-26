package com.avalanches.repository;

import com.avalanches.applicationbusinessrules.usecases.PedidoUseCase;
import com.avalanches.enterprisebusinessrules.entities.Pedido;
import com.avalanches.enterprisebusinessrules.entities.PedidoProduto;
import com.avalanches.enterprisebusinessrules.entities.StatusPedido;
import com.avalanches.interfaceadapters.gateways.interfaces.PedidoGatewayInterface;
import com.avalanches.interfaceadapters.gateways.interfaces.ProdutoGatewayInterface;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PedidoTest {

    @Mock
    private PedidoGatewayInterface pedidoGateway;

    @Mock
    private ProdutoGatewayInterface produtoGateway;

    @InjectMocks
    private PedidoUseCase pedidoUseCase;


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

        pedidoUseCase.cadastrar(pedido, pedidoGateway, produtoGateway);

        //Assert
        verify(pedidoGateway, times(1)).cadastrar(any(Pedido.class));
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

    @Test
    void deveRetornarPedido(){
        //Arrange
        var listaPedidos = new ArrayList<Pedido>();
        var pedido1 = getPedido();
        var pedido2 = getPedido();
        listaPedidos.add(pedido1);
        listaPedidos.add(pedido2);

        //Act

        when(pedidoGateway.listar()).thenReturn(listaPedidos);

        var pedidos = pedidoUseCase.listar(pedidoGateway);

        //Assert
        assertThat(pedidos).hasSizeGreaterThan(0);
    }




}
