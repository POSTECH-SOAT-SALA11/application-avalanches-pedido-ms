package com.avalanches.frameworksanddrivers.api;

import com.avalanches.ProdutoBuilder;
import com.avalanches.enterprisebusinessrules.entities.CategoriaProduto;
import com.avalanches.enterprisebusinessrules.entities.Produto;
import com.avalanches.frameworksanddrivers.api.dto.ProdutoParams;
import com.avalanches.frameworksanddrivers.databases.config.BancoDeDadosContexto;
import com.avalanches.interfaceadapters.controllers.ProdutoController;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class ProdutoApiTest {
    @Mock
    private BancoDeDadosContexto bancoDeDadosContexto;

    @Spy
    private ProdutoController produtoController;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private ProdutoApi produtoApi;

    @Spy
    private ProdutoApi produtoApiSpy;



    @BeforeEach
    void setUp() {
        // Initialize Mockito annotations for mocks and inject mocks
        MockitoAnnotations.openMocks(this);
        when(bancoDeDadosContexto.getJdbcTemplate()).thenReturn(jdbcTemplate);
        ReflectionTestUtils.setField(produtoApiSpy, "bancoDeDadosContexto", bancoDeDadosContexto);
        doReturn(produtoController).when(produtoApiSpy).criarProdutoController();
    }

    @Test
    void testCadastrarProduto_Success() {

        var imagens = ProdutoBuilder.getImagemParams();

        ProdutoParams produtoParams = new ProdutoParams(1,
                new BigDecimal("10.00"),
                10,
                CategoriaProduto.LANCHE,
                "XAvalanche",
                "XAvalanche com molho especial",
                imagens);

        // Simulate controller behavior
        doNothing().when(produtoController).cadastrarProduto(any(Produto.class),any(BancoDeDadosContexto.class));

        // Call the method under test
        ResponseEntity<Void> response = produtoApiSpy.cadastrar(produtoParams);

        // Verify controller invocation
        verify(produtoController, times(1)).cadastrarProduto(any(Produto.class),any(BancoDeDadosContexto.class));

        // Validate response
        assertNotNull(response);
        assertEquals(201, response.getStatusCode().value());
    }


    @Test
    void testAtualizarProduto_Success(){

        var imagens = ProdutoBuilder.getImagemParams();

        ProdutoParams produtoParams = new ProdutoParams(1,
                new BigDecimal("10.00"),
                10,
                CategoriaProduto.LANCHE,
                "XAvalanche",
                "XAvalanche com molho especial",
                imagens);


        doNothing().when(produtoController).atualizarProduto(any(Produto.class),any(BancoDeDadosContexto.class));

        // Call the method under test
        ResponseEntity<Void> response = produtoApiSpy.atualizar(1,produtoParams);

        // Verify controller invocation
        verify(produtoController, times(1)).atualizarProduto(any(Produto.class),any(BancoDeDadosContexto.class));

        // Validate response
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void testExcluirProduto_Success(){

        doNothing().when(produtoController).excluirProduto(anyInt(),any(BancoDeDadosContexto.class));

        // Call the method under test
        ResponseEntity<Void> response = produtoApiSpy.excluir(1);

        // Verify controller invocation
        verify(produtoController, times(1)).excluirProduto(anyInt(),any(BancoDeDadosContexto.class));

        // Validate response
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
    }

}
