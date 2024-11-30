package com.avalanches.frameworksanddrivers.api;

import com.avalanches.enterprisebusinessrules.entities.StatusPedido;
import com.avalanches.frameworksanddrivers.databases.config.BancoDeDadosContexto;
import com.avalanches.interfaceadapters.controllers.PedidoController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PedidoApiTest {

    @Mock
    private BancoDeDadosContexto bancoDeDadosContexto;

    @Spy
    private PedidoController pedidoController;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private PedidoApi pedidoApi;

    @Spy
    private PedidoApi pedidoApiSpy;



    @BeforeEach
    void setUp() {
        // Initialize Mockito annotations for mocks and inject mocks
        MockitoAnnotations.openMocks(this);
        when(bancoDeDadosContexto.getJdbcTemplate()).thenReturn(jdbcTemplate);
        ReflectionTestUtils.setField(pedidoApiSpy, "bancoDeDadosContexto", bancoDeDadosContexto);
        doReturn(pedidoController).when(pedidoApiSpy).criarPedidoController();
    }

//    @Test
//    void testCadastrarPedido_Success() {
//
//        PedidoParams pedidoParams = new PedidoParams(new BigDecimal("100.00"),
//                LocalDateTime.now(),
//                LocalDateTime.now(),
//                PedidoBuilder.getPedidoProdutos(),
//                1);
//
//        int idPedido = 1;
//
//
//        // Simulate controller behavior
//        when(pedidoController.cadastrar(any(Pedido.class), any(BancoDeDadosContexto.class))).thenReturn(idPedido);
//
//        // Call the method under test
//        ResponseEntity<Integer> response = pedidoApiSpy.cadastrar(pedidoParams);
//
//        // Verify controller invocation
//        verify(pedidoController, times(1)).cadastrar(any(Pedido.class),any(BancoDeDadosContexto.class));
//
//        // Validate response
//        assertNotNull(response);
//        assertEquals(201, response.getStatusCode().value());
//    }


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
}
