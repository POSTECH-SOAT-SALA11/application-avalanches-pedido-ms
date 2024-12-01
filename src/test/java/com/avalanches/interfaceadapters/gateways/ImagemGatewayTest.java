package com.avalanches.interfaceadapters.gateways;

import com.avalanches.enterprisebusinessrules.entities.Imagem;
import com.avalanches.frameworksanddrivers.databases.config.BancoDeDadosContexto;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ImagemGatewayTest {

    @Spy
    @InjectMocks
    private ImagemGateway imagemGateway;

   @Mock
    private ImagemGateway imagemGatewayMock;

    @Mock
    private BancoDeDadosContexto bancoDeDadosContexto;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private Files files;


    AutoCloseable openMocks;

    @BeforeEach
    void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
        when(bancoDeDadosContexto.getJdbcTemplate()).thenReturn(jdbcTemplate);
        imagemGateway = new ImagemGateway(bancoDeDadosContexto);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

//    @Test
//    void deveCadastrar(){
//        //Arrange
//
//        var imagens = getImagens();
//        int imagemCount = imagens.size();
//
//        //Act
//        doNothing().when(imagemGateway).cadastrar(any(Imagem.class));
//
//        for (Imagem imagem: imagens) {
//            imagemGateway.cadastrar(imagem);
//        }
//
//        //Assert
//        verify(imagemGateway, times(imagemCount)).cadastrar(any(Imagem.class));
//    }

//    @Test
//    void deveAtualizar(){
//        //Arrange
//
//        var imagem = getImagens().get(0);
//
//        //Act
//
////        doNothing().when(imagemGateway).editarArquivo(imagem);
//
//        Path path = mock(Path.class);
//
//        when(files.exists(eq(path))).thenReturn(true);
//
//        when(jdbcTemplate.update(anyString(), any(), any(), any(), any(), any(), any())).thenReturn(1);
//
//        imagemGateway.atualizar(imagem);
//
//        //Assert
//        verify(jdbcTemplate, times(1)).update(anyString(), any(), any(), any(), any(), any(), any());
//    }
//
    @Test
    void deveExcluir(){
        //Arrange

       var imagem = getImagens().get(0);

        //Act
        when(jdbcTemplate.update(anyString(), anyInt())).thenReturn(1);

        imagemGateway.excluir(imagem);

        //Assert
        verify(jdbcTemplate, times(1)).update(anyString(), anyInt());
    }

//    @Test
//    void deveLerArquivo_NotFound(){
//        String IMAGENS = "imagens";
//        String path ="C:/Imagens";
//        byte[] bytesImagem = new byte[]{69, 121, 101, 45, 62, 118, 101, 114, (byte) 196, (byte) 195, 61, 101, 98};
//
////        Path imagePath = Paths.get(IMAGENS + '/' + path);
//
//        //Act
//
//        when(imagemGateway.lerArquivo(anyString())).thenThrow(new RuntimeException(path));
//
////        var arquivo = imagemGateway.lerArquivo(path);
//
//        //Assert
////        assertThat(arquivo).isNotNull();
//
//        assertThrows(RuntimeException.class, () -> imagemGateway.lerArquivo(path));
//    }


    @Test
    void deveLerArquivo(){
        String path ="C:/Imagens";
        byte[] bytesImagem = new byte[]{69, 121, 101, 45, 62, 118, 101, 114, (byte) 196, (byte) 195, 61, 101, 98};

        //Act

        when(imagemGatewayMock.lerArquivo(anyString())).thenReturn(bytesImagem);

        var arquivo = imagemGatewayMock.lerArquivo(path);

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
