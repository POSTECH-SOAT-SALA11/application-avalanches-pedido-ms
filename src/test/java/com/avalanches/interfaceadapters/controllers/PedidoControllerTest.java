package com.avalanches.interfaceadapters.controllers;

import com.avalanches.PedidoBuilder;
import com.avalanches.applicationbusinessrules.usecases.PedidoUseCase;
import com.avalanches.enterprisebusinessrules.entities.Pedido;
import com.avalanches.enterprisebusinessrules.entities.StatusPedido;
import com.avalanches.frameworksanddrivers.databases.interfaces.BancoDeDadosContextoInterface;
import com.avalanches.interfaceadapters.gateways.PedidoGateway;
import com.avalanches.interfaceadapters.gateways.ProdutoGateway;
import com.avalanches.interfaceadapters.presenters.JsonPresenter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PedidoControllerTest{
    @Mock
    private BancoDeDadosContextoInterface bancoDeDadosContexto;

    @Mock
    private PedidoGateway pedidoGateway;

    @Mock
    private ProdutoGateway produtoGateway;

    @InjectMocks
    private PedidoController pedidoController;

    @Spy
    private PedidoController pedidoControllerSpy;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Spy
    private JsonPresenter jsonPresenter;

    @Mock
    private PedidoUseCase pedidoUseCase;


    AutoCloseable openMocks;

    @BeforeEach
    void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
        when(bancoDeDadosContexto.getJdbcTemplate()).thenReturn(jdbcTemplate);
        pedidoController = new PedidoController();
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
        doReturn(pedidoUseCase).when(pedidoControllerSpy).criarPedidoUseCase();  // Mock method
        doReturn(pedidoGateway).when(pedidoControllerSpy).criarPedidoGateway(bancoDeDadosContexto,jsonPresenter);  // Mock method
        doReturn(produtoGateway).when(pedidoControllerSpy).criarProdutoGateway(bancoDeDadosContexto);  // Mock method
        when(produtoGateway.verificaProdutoExiste(anyInt())).thenReturn(true);

        when(pedidoUseCase.cadastrar(any(Pedido.class), any(PedidoGateway.class),any(ProdutoGateway.class))).thenReturn(pedido.getId());

        pedidoControllerSpy.cadastrar(pedido, bancoDeDadosContexto);

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
        doReturn(pedidoUseCase).when(pedidoControllerSpy).criarPedidoUseCase();  // Mock method
        doReturn(pedidoGateway).when(pedidoControllerSpy).criarPedidoGateway(bancoDeDadosContexto,jsonPresenter); // Mock method

        when(pedidoUseCase.listar(any(PedidoGateway.class))).thenReturn(listaPedidos);
        when(pedidoGateway.listar()).thenReturn(listaPedidos);

        var pedidos = pedidoControllerSpy.listar(bancoDeDadosContexto);

        //Assert
        assertThat(pedidos).hasSizeGreaterThan(0);
    }

    @Test
    void deveAtualizarStatus(){
        //Arrange
        int idPedido = 1;
        var statusPedido = StatusPedido.PRONTO;

        //Act
        doReturn(pedidoUseCase).when(pedidoControllerSpy).criarPedidoUseCase();  // Mock method
        doReturn(pedidoGateway).when(pedidoControllerSpy).criarPedidoGateway(bancoDeDadosContexto,jsonPresenter);  // Mock method
        when(produtoGateway.verificaProdutoExiste(anyInt())).thenReturn(true);

        doNothing().when(pedidoGateway).atualizaStatus(anyInt(),any(StatusPedido.class));
        doNothing().when(pedidoUseCase).atualizaStatus(anyInt(), any(StatusPedido.class), any(PedidoGateway.class));

        pedidoControllerSpy.atualizaStatus(idPedido, statusPedido, bancoDeDadosContexto);

        //Assert
        verify(pedidoUseCase, times(1)).atualizaStatus(anyInt(), any(StatusPedido.class), any(PedidoGateway.class));
    }
}
