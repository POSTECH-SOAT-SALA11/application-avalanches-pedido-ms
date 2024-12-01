package com.avalanches.interfaceadapters.gateways;

import com.avalanches.PedidoBuilder;
import com.avalanches.enterprisebusinessrules.entities.Pedido;
import com.avalanches.enterprisebusinessrules.entities.PedidoProduto;
import com.avalanches.enterprisebusinessrules.entities.StatusPedido;
import com.avalanches.frameworksanddrivers.databases.config.BancoDeDadosContexto;
import com.avalanches.interfaceadapters.presenters.JsonPresenter;
import io.lettuce.core.api.sync.RedisCommands;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PedidoGatewayTest {

    @Mock
    private PedidoGateway pedidoGatewayMock;

    @Spy
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

    @Mock
    private GeneratedKeyHolder keyHolder;



    AutoCloseable openMocks;

    @BeforeEach
    void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
        when(bancoDeDadosContexto.getJdbcTemplate()).thenReturn(jdbcTemplate);
        when(bancoDeDadosContexto.getRedisCommands()).thenReturn(redisCommands);
        pedidoGateway = new PedidoGateway(bancoDeDadosContexto, jsonPresenter, keyHolder);

    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void deveRegistrarPedido(){
        //Arrange

        var pedido = PedidoBuilder.getPedido();

        //Act
        Map<String, Object> generatedKeys = new HashMap<>();
        generatedKeys.put("id", 1);
        when(keyHolder.getKeys()).thenReturn(generatedKeys);

        when(jdbcTemplate.update(any(PreparedStatementCreator.class), eq(keyHolder))).thenReturn(1);

        pedidoGateway.cadastrar(pedido);

        ArgumentCaptor<KeyHolder> keyHolderCaptor = ArgumentCaptor.forClass(KeyHolder.class);
        verify(jdbcTemplate).update(any(PreparedStatementCreator.class), keyHolderCaptor.capture());
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
        when(jdbcTemplate.query(anyString(),any(PedidoGateway.PedidoResultSetExtractor.class))).thenReturn(listaPedidos);

        var pedidos = pedidoGateway.listar();

        //Assert
        assertThat(pedidos).hasSizeGreaterThan(0);
    }

    @Test
    void deveCadastrarProdutosPorPedido()
    {
        //Arrange
        var pedido = getPedido();

        //Act

        when(jdbcTemplate.update(anyString(),anyInt(),anyInt(),anyInt(),any(BigDecimal.class))).thenReturn(1);

        for(PedidoProduto p: pedido.getListaProduto())
            pedidoGateway.cadastrarProdutosPorPedido(pedido.getId(), p);

        //Assert
        verify(jdbcTemplate, times(pedido.getListaProduto().size())).update(anyString(),anyInt(),anyInt(),anyInt(),any(BigDecimal.class));
    }

    @Test
    void deveAtualizarStatus(){
        //Arrange

        //Act

        when(jdbcTemplate.update(anyString(),anyString(), anyInt())).thenReturn(1);
        when(redisCommands.get(anyString())).thenReturn("Pedido 1");
        when(jsonPresenter.deserialize(anyString(), eq(Pedido.class))).thenReturn(PedidoBuilder.getPedido());

        pedidoGateway.atualizaStatus(1, StatusPedido.PRONTO);

        //Assert
        verify(jdbcTemplate, times(1)).update(anyString(),anyString(),anyInt());
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
    void deveVerificarPedidoNaoExiste_Redis(){

        //Act
        when(jdbcTemplate.queryForObject(
                anyString(),
                any(Object[].class),
                eq(Integer.class)
        )).thenReturn(0);

        when(redisCommands.exists(anyString())).thenReturn(0L);

        boolean pedidoExiste = pedidoGateway.verificaPedidoExiste(1);

        //Assert

        assertThat(pedidoExiste).isFalse();
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
        var pedido = PedidoBuilder.getPedido();

        when(jdbcTemplate.queryForObject(
                anyString(),
                any(Object[].class),
                eq(String.class)
        )).thenReturn(null);

        when(redisCommands.get(anyString())).thenReturn("Recebido");
        when(jsonPresenter.deserialize(anyString(), eq(Pedido.class))).thenReturn(pedido);

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
