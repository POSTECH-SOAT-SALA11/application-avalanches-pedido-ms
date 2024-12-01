package com.avalanches.applicationbusinessrules.usecases;

import com.avalanches.ProdutoBuilder;
import com.avalanches.enterprisebusinessrules.entities.CategoriaProduto;
import com.avalanches.enterprisebusinessrules.entities.Produto;
import com.avalanches.interfaceadapters.gateways.interfaces.ImagemGatewayInterface;
import com.avalanches.interfaceadapters.gateways.interfaces.ProdutoGatewayInterface;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProdutoUseCaseTest {

    @Mock
    private ProdutoGatewayInterface produtoGateway;

    @Mock
    private ImagemGatewayInterface imagemGateway;

    @InjectMocks
    private ProdutoUseCase produtoUseCase;

    AutoCloseable openMocks;

    @BeforeEach
    void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
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
        doNothing().when(produtoGateway).cadastrar(any(Produto.class));

        produtoUseCase.cadastrarProduto(produto, produtoGateway, imagemGateway);

        //Assert
        verify(produtoGateway, times(1)).cadastrar(any(Produto.class));
    }


    @Test
    void deveAtualizarProduto(){
        //Arrange

        var produto = ProdutoBuilder.getProduto();

        //Act
        doNothing().when(produtoGateway).atualizar(any(Produto.class));
        when(produtoGateway.consultarProdutosPorID(anyInt())).thenReturn(produto);

        produtoUseCase.atualizarProduto(produto, produtoGateway, imagemGateway);

        //Assert
        verify(produtoGateway, times(1)).atualizar(any(Produto.class));
    }

    @Test
    void deveExcluirProduto(){
        //Arrange
        var idProduto = 1;
        var produto =  ProdutoBuilder.getProduto();

        //Act
        doNothing().when(produtoGateway).excluir(anyInt());
        when(produtoGateway.consultarProdutosPorID(anyInt())).thenReturn(produto);

        produtoUseCase.excluirProduto(idProduto, produtoGateway, imagemGateway);

        //Assert
        verify(produtoGateway, times(1)).excluir(anyInt());;
    }

    @Test
    void deveConsultarProdutos(){
        //Arrange
        var listaProdutos = new ArrayList<Produto>();
        var produto1 = ProdutoBuilder.getProduto();
        var produto2 =  ProdutoBuilder.getProduto();
        listaProdutos.add(produto1);
        listaProdutos.add(produto2);

        //Act

        when(produtoGateway.consultarProdutos(any(CategoriaProduto.class))).thenReturn(listaProdutos);

        var produtos = produtoUseCase.consultarProdutos(CategoriaProduto.LANCHE, produtoGateway, imagemGateway);

        //Assert
        assertThat(produtos).hasSizeGreaterThan(0);
    }

}
