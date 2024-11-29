package com.avalanches.repository;

import com.avalanches.enterprisebusinessrules.entities.*;
import com.avalanches.interfaceadapters.gateways.interfaces.ProdutoGatewayInterface;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProdutoRepositoryTest {
    @Mock
    private ProdutoGatewayInterface produtoGateway;

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

        var produto = getProduto();

        //Act
        doNothing().when(produtoGateway).cadastrar(any(Produto.class));

        produtoGateway.cadastrar(produto);

        //Assert
        verify(produtoGateway, times(1)).cadastrar(any(Produto.class));
    }

    @Test
    void deveCadastrarImagemPorProduto(){
        //Arrange
        int idProduto = 1;
        int idImagem = 1;

        //Act
        doNothing().when(produtoGateway).cadastrarImagemProduto(anyInt(),anyInt());

        produtoGateway.cadastrarImagemProduto(idProduto,idImagem);

        //Assert
        verify(produtoGateway, times(1)).cadastrarImagemProduto(anyInt(),anyInt());;
    }

    @Test
    void deveAtualizar(){
        //Arrange
        var produto = getProduto();

        //Act
        doNothing().when(produtoGateway).atualizar(any(Produto.class));

        produtoGateway.atualizar(produto);

        //Assert
        verify(produtoGateway, times(1)).atualizar(any(Produto.class));;
    }

    @Test
    void deveExcluir(){
        //Arrange
        var idProduto = 1;

        //Act
        doNothing().when(produtoGateway).excluir(anyInt());

        produtoGateway.excluir(idProduto);

        //Assert
        verify(produtoGateway, times(1)).excluir(anyInt());;
    }

    @Test
    void deveExcluirImagemPorProduto(){
        //Arrange
        int idProduto = 1;
        int idImagem = 1;
        //Act
        doNothing().when(produtoGateway).excluirImagemProduto(anyInt(),anyInt());

        produtoGateway.excluirImagemProduto(idProduto,idImagem);

        //Assert
        verify(produtoGateway, times(1)).excluirImagemProduto(anyInt(),anyInt());
    }

    @Test
    void deveConsultarProdutos(){
        //Arrange
        var listaProdutos = new ArrayList<Produto>();
        var produto1 = getProduto();
        var produto2 = getProduto();
        listaProdutos.add(produto1);
        listaProdutos.add(produto2);

        //Act

        when(produtoGateway.consultarProdutos(any(CategoriaProduto.class))).thenReturn(listaProdutos);

        var produtos = produtoGateway.consultarProdutos(CategoriaProduto.LANCHE);

        //Assert
        assertThat(produtos).hasSizeGreaterThan(0);
    }

    @Test
    void deveConsultarProdutosPorID(){
        //Arrange
        int idProduto = 1;

        //Act

        when(produtoGateway.consultarProdutosPorID(anyInt())).thenReturn(getProduto());

        var produto = produtoGateway.consultarProdutos(CategoriaProduto.LANCHE);

        //Assert
        assertThat(produto).isNotNull();
    }

    @Test
    void deveConsultarImagensPorProduto(){
        //Arrange
        int idProduto = 1;
        var imagens = getImagens();

        //Act

        when(produtoGateway.consultarImagensPorProduto(anyInt())).thenReturn(imagens);

        var imagensPorProduto = produtoGateway.consultarImagensPorProduto(idProduto);

        //Assert
        assertThat(imagensPorProduto).hasSizeGreaterThan(0);
    }

    @Test
    void deveVerificarProdutoExiste(){

        //Act
        when(produtoGateway.verificaProdutoExiste(anyInt())).thenReturn(true);

        boolean produtoExiste = produtoGateway.verificaProdutoExiste(1);

        //Assert

        assertThat(produtoExiste).isTrue();
    }


    public static @NotNull Produto getProduto() {

        var imagens = getImagens();

        Produto produto = new Produto(1,
                new BigDecimal("10.00"),
                10,
                CategoriaProduto.LANCHE,
                "XAvalanche",
                "XAvalanche com molho especial",
                imagens
        );


        return produto;
    }

    private static @NotNull ArrayList<Imagem> getImagens() {

        var listaImagens = new ArrayList<Imagem>();
        var imagem = new Imagem(1,
                "xAvalanche.jpg",
                "xAvalanche1",
                "jpg",
                100,
                "C:/Imagens",
                new byte[] {69, 121, 101, 45, 62, 118, 101, 114, (byte) 196, (byte) 195, 61, 101, 98}
                );

        listaImagens.add(imagem);

        return listaImagens;
    }


}
