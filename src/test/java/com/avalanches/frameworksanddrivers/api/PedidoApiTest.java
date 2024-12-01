package com.avalanches.frameworksanddrivers.api;

import com.avalanches.PedidoBuilder;
import com.avalanches.enterprisebusinessrules.entities.Pedido;
import com.avalanches.enterprisebusinessrules.entities.PedidoProduto;
import com.avalanches.enterprisebusinessrules.entities.StatusPedido;
import com.avalanches.frameworksanddrivers.api.dto.PedidoParams;
import com.avalanches.frameworksanddrivers.databases.config.BancoDeDadosContexto;
import com.avalanches.interfaceadapters.controllers.PedidoController;
import com.avalanches.interfaceadapters.presenters.dtos.PedidoDto;
import com.avalanches.interfaceadapters.presenters.dtos.PedidoProdutoDto;
import com.avalanches.interfaceadapters.presenters.dtos.StatusPedidoDto;
import io.lettuce.core.api.sync.RedisCommands;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PedidoApiTest {

    @Mock
    private BancoDeDadosContexto bancoDeDadosContexto;

    @Mock
    private PedidoController pedidoController;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private RedisCommands<String, String> redisCommands;

    @InjectMocks
    private PedidoApi pedidoApi;

    @Spy
    private PedidoApi pedidoApiSpy;



    @BeforeEach
    void setUp() {
        // Initialize Mockito annotations for mocks and inject mocks
        MockitoAnnotations.openMocks(this);
        when(bancoDeDadosContexto.getJdbcTemplate()).thenReturn(jdbcTemplate);
        when(bancoDeDadosContexto.getRedisCommands()).thenReturn(redisCommands);
        ReflectionTestUtils.setField(pedidoApiSpy, "bancoDeDadosContexto", bancoDeDadosContexto);
        doReturn(pedidoController).when(pedidoApiSpy).criarPedidoController();
    }

    @Test
    void testCadastrarPedido_Success() {

        PedidoParams pedidoParams = new PedidoParams(new BigDecimal("100.00"),
                LocalDateTime.now(),
                LocalDateTime.now(),
                PedidoBuilder.getPedidoProdutos(),
                1);

        int idPedido = 1;


        // Simulate controller behavior
        when(pedidoController.cadastrar(any(Pedido.class), any(BancoDeDadosContexto.class))).thenReturn(idPedido);

        // Call the method under test
        ResponseEntity<Integer> response = pedidoApiSpy.cadastrar(pedidoParams);

        // Verify controller invocation
        verify(pedidoController, times(1)).cadastrar(any(Pedido.class),any(BancoDeDadosContexto.class));

        // Validate response
        assertEquals(Objects.requireNonNull(response.getBody()).intValue(), idPedido);
        assertNotNull(response);
        assertEquals(201, response.getStatusCode().value());
    }


    @Test
    void testAtualizarPedido_Success() {
        //Arrange
        int idPedido = 1;
        StatusPedido statusPedido = StatusPedido.PRONTO;

        // Simulate controller behavior
        doNothing().when(pedidoController).atualizaStatus(anyInt(),any(StatusPedido.class),any(BancoDeDadosContexto.class));

        // Call the method under test
        ResponseEntity<Void> response = pedidoApiSpy.atualizaStatus(idPedido, statusPedido);

        // Verify controller invocation
        verify(pedidoController, times(1)).atualizaStatus(anyInt(),any(StatusPedido.class),any(BancoDeDadosContexto.class));

        // Validate response
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void testListarPedido_Success() {

        var listaPedidos = new ArrayList<PedidoDto>();
        var listaPedidoProdutoDto = new ArrayList<PedidoProdutoDto>();

        var pedidoProduto1 = new PedidoProdutoDto(1,3,new BigDecimal("10"));
        var pedidoProduto2 = new PedidoProdutoDto(2,1,new BigDecimal("20"));

        listaPedidoProdutoDto.add(pedidoProduto1);
        listaPedidoProdutoDto.add(pedidoProduto2);

        Duration durationInMinutes = Duration.ofMinutes(10);

        var pedido1 = new PedidoDto(
                1,
                StatusPedidoDto.EMPREPARACAO,
                new BigDecimal("100.00"),
                LocalDateTime.now(),
                LocalDateTime.now(),
                durationInMinutes,
                listaPedidoProdutoDto,
                1
        );


        pedido1.listaProduto.add(pedidoProduto1);
        pedido1.listaProduto.add(pedidoProduto2);

        listaPedidos.add(pedido1);

        when(pedidoController.listar(any(BancoDeDadosContexto.class))).thenReturn(listaPedidos);

        // Call the method under test
        ResponseEntity<List<PedidoDto>> response = pedidoApiSpy.listar();

        // Validate response
        assertNotNull(response);
        assertTrue(!response.getBody().isEmpty());
        assertEquals(200, response.getStatusCode().value());
    }
}
