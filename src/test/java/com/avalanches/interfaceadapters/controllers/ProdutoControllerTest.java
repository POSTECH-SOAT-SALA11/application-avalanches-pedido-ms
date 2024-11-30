package com.avalanches.interfaceadapters.controllers;

import com.avalanches.ProdutoBuilder;
import com.avalanches.applicationbusinessrules.usecases.ProdutoUseCase;
import com.avalanches.enterprisebusinessrules.entities.CategoriaProduto;
import com.avalanches.enterprisebusinessrules.entities.Produto;
import com.avalanches.frameworksanddrivers.databases.interfaces.BancoDeDadosContextoInterface;
import com.avalanches.interfaceadapters.gateways.ImagemGateway;
import com.avalanches.interfaceadapters.gateways.ProdutoGateway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;

import static com.avalanches.interfaceadapters.gateways.ProdutoGatewayTest.getProduto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProdutoControllerTest {

    @Mock
    private BancoDeDadosContextoInterface bancoDeDadosContexto;

    @Mock
    private ProdutoGateway produtoGateway;

    @Mock
    private ImagemGateway imagemGateway;

    @InjectMocks
    private ProdutoController produtoController;

    @Spy
    private ProdutoController produtoControllerSpy;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private ProdutoUseCase produtoUseCase;

    AutoCloseable openMocks;

    @BeforeEach
    void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
        when(bancoDeDadosContexto.getJdbcTemplate()).thenReturn(jdbcTemplate);
        produtoController = new ProdutoController();
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void deveCadastrarProduto(){
        //Arrange

        var produto = ProdutoBuilder.getProduto();

        //Act
        doReturn(produtoUseCase).when(produtoControllerSpy).criarProdutoUseCase();  // Mock method
        doReturn(produtoGateway).when(produtoControllerSpy).criarProdutoGateway(bancoDeDadosContexto);  // Mock method

        doNothing().when(produtoUseCase).cadastrarProduto(any(Produto.class),any(ProdutoGateway.class),any(ImagemGateway.class));
        doNothing().when(produtoGateway).cadastrar(any(Produto.class));


        produtoControllerSpy.cadastrarProduto(produto, bancoDeDadosContexto);

        //Assert
        verify(produtoUseCase, times(1)).cadastrarProduto(any(Produto.class),any(ProdutoGateway.class),any(ImagemGateway.class));
    }

    @Test
    void deveAtualizarProduto(){
        //Arrange

        var produto = ProdutoBuilder.getProduto();

        //Act
        doReturn(produtoUseCase).when(produtoControllerSpy).criarProdutoUseCase();  // Mock method
        doReturn(produtoGateway).when(produtoControllerSpy).criarProdutoGateway(bancoDeDadosContexto);  // Mock method

        doNothing().when(produtoUseCase).atualizarProduto(any(Produto.class), any(ProdutoGateway.class), any(ImagemGateway.class));
        when(produtoGateway.consultarProdutosPorID(anyInt())).thenReturn(produto);
        doNothing().when(produtoGateway).atualizar(any(Produto.class));

        produtoControllerSpy.atualizarProduto(produto, bancoDeDadosContexto);

        //Assert
        verify(produtoUseCase, times(1)).atualizarProduto(any(Produto.class), any(ProdutoGateway.class), any(ImagemGateway.class));
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
        doReturn(produtoUseCase).when(produtoControllerSpy).criarProdutoUseCase();  // Mock method
        doReturn(produtoGateway).when(produtoControllerSpy).criarProdutoGateway(bancoDeDadosContexto);  // Mock method

        when(produtoGateway.consultarProdutos(any(CategoriaProduto.class))).thenReturn(listaProdutos);
        when(produtoUseCase.consultarProdutos(any(CategoriaProduto.class), any(ProdutoGateway.class),any(ImagemGateway.class))).thenReturn(listaProdutos);

        var produtos = produtoControllerSpy.consultarProdutos(CategoriaProduto.LANCHE, bancoDeDadosContexto);

        //Assert
        assertThat(produtos).hasSizeGreaterThan(0);
    }

    @Test
    void deveExcluirProduto() {
        //Arrange
        var idProduto = 1;

        //Act
        doReturn(produtoUseCase).when(produtoControllerSpy).criarProdutoUseCase();  // Mock method
        doReturn(produtoGateway).when(produtoControllerSpy).criarProdutoGateway(bancoDeDadosContexto);  // Mock method

        doNothing().when(produtoGateway).excluir(anyInt());
        doNothing().when(produtoUseCase).excluirProduto(anyInt(),any(ProdutoGateway.class),any(ImagemGateway.class));
        when(produtoGateway.consultarProdutosPorID(anyInt())).thenReturn(getProduto());

        produtoControllerSpy.excluirProduto(idProduto, bancoDeDadosContexto);

        //Assert
        verify(produtoUseCase, times(1)).excluirProduto(anyInt(),any(ProdutoGateway.class),any(ImagemGateway.class));
    }


}
