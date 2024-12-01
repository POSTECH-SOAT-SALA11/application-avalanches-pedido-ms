package com.avalanches.interfaceadapters.gateways;

import com.avalanches.PedidoBuilder;
import com.avalanches.enterprisebusinessrules.entities.Pedido;
import com.avalanches.enterprisebusinessrules.entities.PedidoProduto;
import com.avalanches.enterprisebusinessrules.entities.StatusPedido;
import com.avalanches.frameworksanddrivers.databases.config.BancoDeDadosContexto;
import com.avalanches.interfaceadapters.gateways.interfaces.PedidoGatewayInterface;
import com.avalanches.interfaceadapters.presenters.JsonPresenter;
import io.lettuce.core.api.sync.RedisCommands;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PedidoGatewayTest {

    @Mock
    private PedidoGateway pedidoGatewayMock;

    @InjectMocks
    private PedidoGateway pedidoGateway;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private RedisCommands<String, String> redisCommands;

    @Mock
    private BancoDeDadosContexto bancoDeDadosContexto;

    @Mock
    private JsonPresenter jsonPresenter;


    AutoCloseable openMocks;

    @BeforeEach
    void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
        when(bancoDeDadosContexto.getJdbcTemplate()).thenReturn(jdbcTemplate);
        when(bancoDeDadosContexto.getRedisCommands()).thenReturn(redisCommands);
        pedidoGateway = new PedidoGateway(bancoDeDadosContexto, jsonPresenter);
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
        doNothing().when(pedidoGatewayMock).cadastrar(any(Pedido.class));

        pedidoGatewayMock.cadastrar(pedido);

        //Assert
        verify(pedidoGatewayMock, times(1)).cadastrar(any(Pedido.class));
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

        when(pedidoGatewayMock.listar()).thenReturn(listaPedidos);

        var pedidos = pedidoGatewayMock.listar();

        //Assert
        assertThat(pedidos).hasSizeGreaterThan(0);
    }

    @Test
    void deveCadastrarProdutosPorPedido()
    {
        //Arrange

        var pedido1 = getPedido();

        //Act

        doNothing().when(pedidoGatewayMock).cadastrarProdutosPorPedido(anyInt(), any(PedidoProduto.class));

        for(PedidoProduto p: pedido1.getListaProduto())
            pedidoGatewayMock.cadastrarProdutosPorPedido(pedido1.getId(), p);

        //Assert
        verify(pedidoGatewayMock, times(pedido1.getListaProduto().size())).cadastrarProdutosPorPedido(anyInt(), any(PedidoProduto.class));
    }

    @Test
    void deveAtualizarStatus(){
        //Arrange
        doNothing().when(pedidoGatewayMock).atualizaStatus(anyInt(),any(StatusPedido.class));
        //Act

        pedidoGatewayMock.atualizaStatus(1, StatusPedido.PRONTO);

        //Assert
        verify(pedidoGatewayMock, times(1)).atualizaStatus(anyInt(),any(StatusPedido.class));
    }

    @Test
    void deveVerificarPedidoExiste_Postgree(){

        //Act
        when(jdbcTemplate.queryForObject(
                anyString(),
                any(Object[].class),
                eq(Integer.class)
        )).thenReturn(1);

        when(redisCommands.exists(anyString())).thenReturn(0L);

        boolean pedidoExiste = pedidoGateway.verificaPedidoExiste(1);

        //Assert

        assertThat(pedidoExiste).isTrue();
    }

    @Test
    void deveVerificarPedidoExiste_Redis(){

        //Act
        when(jdbcTemplate.queryForObject(
                anyString(),
                any(Object[].class),
                eq(Integer.class)
        )).thenReturn(0);

        when(redisCommands.exists(anyString())).thenReturn(1L);

        boolean pedidoExiste = pedidoGateway.verificaPedidoExiste(1);

        //Assert

        assertThat(pedidoExiste).isTrue();
    }

    @Test
    void deveBuscarStatusPedido_Postgree()
    {
        int idPedido = 1;

        when(jdbcTemplate.queryForObject(
                anyString(),
                any(Object[].class),
                eq(String.class)
        )).thenReturn("Em Preparação");

        when(redisCommands.get(anyString())).thenReturn(null);

        String statusPedido = pedidoGateway.buscarStatusPedido(idPedido);

        //Assert
        assertThat(statusPedido).isEqualTo("Em Preparação");
    }

    @Test
    void deveBuscarStatusPedido_Redis()
    {
        int idPedido = 1;

        when(jdbcTemplate.queryForObject(
                anyString(),
                any(Object[].class),
                eq(String.class)
        )).thenReturn(null);

        when(redisCommands.get(anyString())).thenReturn("Recebido");
        when(jsonPresenter.deserialize(anyString(), eq(Pedido.class))).thenReturn(PedidoBuilder.getPedido());

        String statusPedido = pedidoGateway.buscarStatusPedido(idPedido);

        //Assert
        assertThat(statusPedido).isEqualTo("Recebido");
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
