package com.avalanches.frameworksanddrivers.api;

import com.avalanches.ProdutoBuilder;
import com.avalanches.enterprisebusinessrules.entities.CategoriaProduto;
import com.avalanches.enterprisebusinessrules.entities.Imagem;
import com.avalanches.enterprisebusinessrules.entities.Produto;
import com.avalanches.frameworksanddrivers.api.dto.ProdutoParams;
import com.avalanches.frameworksanddrivers.databases.config.BancoDeDadosContexto;
import com.avalanches.interfaceadapters.controllers.ProdutoController;
import com.avalanches.interfaceadapters.presenters.dtos.CategoriaProdutoDto;
import com.avalanches.interfaceadapters.presenters.dtos.ImagemDto;
import com.avalanches.interfaceadapters.presenters.dtos.ProdutoDto;
import org.jetbrains.annotations.NotNull;
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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class ProdutoApiTest {
    @Mock
    private BancoDeDadosContexto bancoDeDadosContexto;

    @Mock
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

    @Test
    void testConsultarPorCategoria_Success(){
        //Arrange
        var listaProdutos = new ArrayList<ProdutoDto>();

        var produtoDto = new ProdutoDto(1,
                new BigDecimal("10.00"),
                10,
                CategoriaProdutoDto.LANCHE,
                "XAvalanche",
                "XAvalanche com molho especial",
                getImagens()
        );

        listaProdutos.add(produtoDto);


        when(produtoController.consultarProdutos(any(CategoriaProduto.class),any(BancoDeDadosContexto.class))).thenReturn(listaProdutos);

        // Call the method under test
        ResponseEntity<List<ProdutoDto>> response = produtoApiSpy.consultarPorCategoria(CategoriaProduto.LANCHE);

        // Validate response
        assertNotNull(response);
        assertTrue(!response.getBody().isEmpty());
        assertEquals(200, response.getStatusCode().value());

    }

    public static @NotNull ArrayList<ImagemDto> getImagens() {

        var listaImagens = new ArrayList<ImagemDto>();
        var imagem = new ImagemDto(1,
                "xAvalanche.jpg",
                "xAvalanche1",
                "jpg",
                100,
                "C:/Imagens",
                new byte[]{69, 121, 101, 45, 62, 118, 101, 114, (byte) 196, (byte) 195, 61, 101, 98}
        );

        listaImagens.add(imagem);

        return listaImagens;
    }

}
