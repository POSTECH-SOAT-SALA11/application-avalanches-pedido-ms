package com.avalanches.repository;

import com.avalanches.enterprisebusinessrules.entities.Imagem;
import com.avalanches.interfaceadapters.gateways.interfaces.ImagemGatewayInterface;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ImagemGatewayTest {

    @Mock
    private ImagemGatewayInterface imagemGateway;


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
    void deveCadastrar(){
        //Arrange

        var imagens = getImagens();
        int imagemCount = imagens.size();

        //Act
        doNothing().when(imagemGateway).cadastrar(any(Imagem.class));

        for (Imagem imagem: imagens) {
            imagemGateway.cadastrar(imagem);
        }

        //Assert
        verify(imagemGateway, times(imagemCount)).cadastrar(any(Imagem.class));
    }

    @Test
    void deveAtualizar(){
        //Arrange

        var imagens = getImagens();

        //Act
        doNothing().when(imagemGateway).atualizar(any(Imagem.class));

        imagemGateway.atualizar(imagens.get(0));

        //Assert
        verify(imagemGateway, times(1)).atualizar(any(Imagem.class));
    }

    @Test
    void deveExcluir(){
        //Arrange

       var imagem = getImagens().get(0);

        //Act
        doNothing().when(imagemGateway).excluir(any(Imagem.class));

        imagemGateway.atualizar(imagem);

        //Assert
        verify(imagemGateway, times(1)).atualizar(any(Imagem.class));
    }

    @Test
    void deveLerArquivo(){
        String path ="C:/Imagens";
        byte[] bytesImagem = new byte[]{69, 121, 101, 45, 62, 118, 101, 114, (byte) 196, (byte) 195, 61, 101, 98};

        //Act

        when(imagemGateway.lerArquivo(anyString())).thenReturn(bytesImagem);

        var arquivo = imagemGateway.lerArquivo(path);

        //Assert
        assertThat(arquivo).isNotNull();
    }



    private static @NotNull ArrayList<Imagem> getImagens() {

        var listaImagens = new ArrayList<Imagem>();
        var imagem = new Imagem(1,
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
