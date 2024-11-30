package com.avalanches.interfaceadapters.controllers;

import com.avalanches.PedidoBuilder;
import com.avalanches.applicationbusinessrules.usecases.PedidoUseCase;
import com.avalanches.enterprisebusinessrules.entities.Pedido;
import com.avalanches.enterprisebusinessrules.entities.StatusPedido;
import com.avalanches.frameworksanddrivers.databases.interfaces.BancoDeDadosContextoInterface;
import com.avalanches.interfaceadapters.gateways.PedidoGateway;
import com.avalanches.interfaceadapters.gateways.ProdutoGateway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PedidoControllerTest{
    @Mock
    private BancoDeDadosContextoInterface bancoDeDadosContexto;

    @Mock
    private PedidoGateway pedidoGateway;

    @Mock
    private ProdutoGateway produtoGateway;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private PedidoUseCase pedidoUseCase;


    AutoCloseable openMocks;

    @BeforeEach
    void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
        when(bancoDeDadosContexto.getJdbcTemplate()).thenReturn(jdbcTemplate);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void deveCadastrar() {

        //Arrange
        var pedido = PedidoBuilder.getPedido();

        //Act
        when(pedidoUseCase.cadastrar(any(Pedido.class), any(PedidoGateway.class),any(ProdutoGateway.class))).thenReturn(pedido.getId());
        pedidoUseCase.cadastrar(pedido, pedidoGateway, produtoGateway);

        //Assert
        verify(pedidoUseCase, times(1)).cadastrar(any(Pedido.class), any(PedidoGateway.class), any(ProdutoGateway.class));
    }

    @Test
    void deveListarPedido(){
        //Arrange
        var listaPedidos = new ArrayList<Pedido>();
        var pedido1 = PedidoBuilder.getPedido();
        var pedido2 = PedidoBuilder.getPedido();
        listaPedidos.add(pedido1);
        listaPedidos.add(pedido2);

        //Act

        when(pedidoUseCase.listar(pedidoGateway)).thenReturn(listaPedidos);

        var pedidos = pedidoUseCase.listar(pedidoGateway);

        //Assert
        assertThat(pedidos).hasSizeGreaterThan(0);
    }

    @Test
    void deveAtualizarStatus(){
        //Arrange
        int idPedido = 1;
        var statusPedido = StatusPedido.PRONTO;

        //Act
        doNothing().when(pedidoGateway).atualizaStatus(anyInt(),any(StatusPedido.class));

        doNothing().when(pedidoUseCase).atualizaStatus(anyInt(), any(StatusPedido.class), any(PedidoGateway.class));

        pedidoUseCase.atualizaStatus(idPedido, statusPedido, pedidoGateway);

        //Assert
        verify(pedidoUseCase, times(1)).atualizaStatus(anyInt(), any(StatusPedido.class), any(PedidoGateway.class));
    }
}
