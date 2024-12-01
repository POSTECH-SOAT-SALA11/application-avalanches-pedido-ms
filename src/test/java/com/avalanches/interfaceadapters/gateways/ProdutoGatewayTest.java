package com.avalanches.interfaceadapters.gateways;

import com.avalanches.ProdutoBuilder;
import com.avalanches.enterprisebusinessrules.entities.*;
import com.avalanches.frameworksanddrivers.databases.config.BancoDeDadosContexto;
import com.avalanches.interfaceadapters.gateways.mapper.ImagemRowMapper;
import com.avalanches.interfaceadapters.gateways.mapper.ProdutoRowMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProdutoGatewayTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    private List<Imagem> imagens;

    @Mock
    private BancoDeDadosContexto bancoDeDadosContexto;

    @Spy
    @InjectMocks
    private ProdutoGateway produtoGateway;

    @Mock
    private GeneratedKeyHolder keyHolder;

    AutoCloseable openMocks;

    @BeforeEach
    void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
        when(bancoDeDadosContexto.getJdbcTemplate()).thenReturn(jdbcTemplate);
        produtoGateway = new ProdutoGateway(bancoDeDadosContexto, keyHolder);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void deveCadastrarProduto() {

        imagens = List.of(
                new Imagem(100, "imagem1.png", "Descrição 1", "image/png", 2048, "/caminho/imagem1.png", new byte[0]),
                new Imagem(101, "imagem2.jpg", "Descrição 2", "image/jpeg", 3072, "/caminho/imagem2.jpg", new byte[0])
        );

        Produto produto = new Produto(
                1,
                new BigDecimal("23.90"),
                10,
                CategoriaProduto.LANCHE,
                "X-Burger",
                "X-Burger xpto",
                imagens
        );

        Map<String, Object> generatedKeys = new HashMap<>();
        generatedKeys.put("id", 1);
        when(keyHolder.getKeys()).thenReturn(generatedKeys);

        when(jdbcTemplate.update(any(), eq(keyHolder))).thenReturn(1);

        produtoGateway.cadastrar(produto);

        ArgumentCaptor<KeyHolder> keyHolderCaptor = ArgumentCaptor.forClass(KeyHolder.class);
        verify(jdbcTemplate).update(any(), keyHolderCaptor.capture());

        assertNotNull(produto.getId());
        assertEquals(1, produto.getId());
    }

    @Test
    void deveCadastrarImagemPorProduto(){
        //Arrange
        int idProduto = 1;
        int idImagem = 1;

        //Act
        when(jdbcTemplate.update(anyString(), eq(idProduto), eq(idImagem))).thenReturn(1);

        produtoGateway.cadastrarImagemProduto(idProduto,idImagem);

        //Assert
        verify(jdbcTemplate, times(1)).update(anyString(), eq(idProduto), eq(idImagem));
    }

    @Test
    void deveAtualizar(){
        //Arrange
        var produto = ProdutoBuilder.getProduto();

        //Act
        when(jdbcTemplate.update(anyString(), anyString(),anyString(),anyString(),anyInt(),any(BigDecimal.class),any(Integer.class))).thenReturn(1);

        produtoGateway.atualizar(produto);

        //Assert
        verify(jdbcTemplate, times(1)).update(anyString(), anyString(),anyString(),anyString(),anyInt(),any(BigDecimal.class),any(Integer.class));
    }

    @Test
    void deveExcluir(){
        //Arrange
        var idProduto = 1;

        when(jdbcTemplate.update(anyString(), eq(idProduto))).thenReturn(1);

        //Act
       produtoGateway.excluir(idProduto);

        //Assert
        verify(jdbcTemplate, times(1)).update(anyString(), eq(idProduto));
    }

    @Test
    void deveExcluirImagemPorProduto(){
        //Arrange
        int idProduto = 1;
        int idImagem = 1;
        //Act
        when(jdbcTemplate.update(anyString(), eq(idProduto), eq(idImagem))).thenReturn(1);

        produtoGateway.excluirImagemProduto(idProduto,idImagem);

        //Assert
        verify(jdbcTemplate, times(1)).update(anyString(), eq(idProduto), eq(idImagem));
    }

    @Test
    void deveConsultarProdutos(){
        //Arrange
        var listaProdutos = new ArrayList<Produto>();
        var produto1 = ProdutoBuilder.getProduto();
        var produto2 = ProdutoBuilder.getProduto();
        listaProdutos.add(produto1);
        listaProdutos.add(produto2);

        //Act
        when(jdbcTemplate.query(any(String.class),
                any(ProdutoRowMapper.class),
                anyString()
        )).thenReturn(listaProdutos);

        var produtos = produtoGateway.consultarProdutos(CategoriaProduto.LANCHE);

        //Assert
        assertThat(produtos).hasSizeGreaterThan(0);
    }

    @Test
    void deveConsultarProdutosPorID(){
        //Arrange
        int idProduto = 1;
        var produto = ProdutoBuilder.getProduto();

        //Act
        when(jdbcTemplate.queryForObject(any(String.class),
                any(ProdutoRowMapper.class),
                anyInt()
        )).thenReturn(produto);

        var produtoRetornado = produtoGateway.consultarProdutosPorID(idProduto);

        //Assert
        assertThat(produtoRetornado).isNotNull();
    }

    @Test
    void deveConsultarImagensPorProduto(){
        //Arrange
        int idProduto = 1;
        var imagens = ProdutoBuilder.getImagens();

        //Act

        when(jdbcTemplate.query(any(String.class),
                any(ImagemRowMapper.class),
                anyInt()
        )).thenReturn(imagens);

        var imagensPorProduto = produtoGateway.consultarImagensPorProduto(idProduto);

        //Assert
        assertThat(imagensPorProduto).hasSizeGreaterThan(0);
    }

    @Test
    void deveVerificarProdutoExiste(){

        when(jdbcTemplate.queryForObject(
                eq("SELECT COUNT(*) FROM produto WHERE id = ?"),
                any(Object[].class),
                eq(Integer.class)
        )).thenReturn(1);

        boolean produtoExiste = produtoGateway.verificaProdutoExiste(1);

        //Assert
        assertThat(produtoExiste).isTrue();
    }

}
