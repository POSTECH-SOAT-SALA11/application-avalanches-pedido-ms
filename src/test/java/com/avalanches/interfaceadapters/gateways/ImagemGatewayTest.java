package com.avalanches.interfaceadapters.gateways;

import com.avalanches.enterprisebusinessrules.entities.Imagem;
import com.avalanches.frameworksanddrivers.databases.config.BancoDeDadosContexto;
import io.lettuce.core.api.sync.RedisCommands;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.webjars.NotFoundException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.avalanches.interfaceadapters.gateways.ImagemGateway.IMAGENS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ImagemGatewayTest {

    @Spy
    @InjectMocks
    private ImagemGateway imagemGateway;

    @Mock
    private BancoDeDadosContexto bancoDeDadosContexto;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private GeneratedKeyHolder keyHolder;

    AutoCloseable openMocks;

    @BeforeEach
    void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
        when(bancoDeDadosContexto.getJdbcTemplate()).thenReturn(jdbcTemplate);
        imagemGateway = new ImagemGateway(bancoDeDadosContexto, keyHolder);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void deveCadastrarImagem() throws SQLException {
        Imagem imagem = new Imagem(1, "imagem2.jpg", "Descrição 2", "image/jpeg", 3072, "/caminho/imagem2.jpg", new byte[0]);

        Map<String, Object> generatedKeys = new HashMap<>();
        generatedKeys.put("id", 1);
        when(keyHolder.getKeys()).thenReturn(generatedKeys);

        when(jdbcTemplate.update(any(PreparedStatementCreator.class), eq(keyHolder))).thenReturn(1);

        imagemGateway.cadastrar(imagem);

        ArgumentCaptor<PreparedStatementCreator> statementCaptor = ArgumentCaptor.forClass(PreparedStatementCreator.class);

        verify(jdbcTemplate).update(statementCaptor.capture(), eq(keyHolder));

        PreparedStatementCreator preparedStatementCreator = statementCaptor.getValue();

        Connection connection = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(ps);

        PreparedStatement preparedStatement = preparedStatementCreator.createPreparedStatement(connection);

        assertNotNull(preparedStatement);

        verify(ps).setString(1, imagem.getNome());
        verify(ps).setString(2, imagem.getDescricao());
        verify(ps).setString(3, imagem.getTipoConteudo());
        verify(ps).setString(4, imagem.getCaminho());
        verify(ps).setInt(5, imagem.getTamanho());

        assertEquals(1, imagem.getId());
    }

    @Test
    void deveAtualizarImagem() {
        Imagem imagem = new Imagem(1, "imagem2.jpg", "Descrição 2", "image/jpeg", 3072, "/caminho/imagem2.jpg", new byte[0]);

        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            Path imagePath = Paths.get(IMAGENS, "arquivo.jpg");

            mockedFiles.when(() -> Files.exists(Paths.get(IMAGENS))).thenReturn(true);
            mockedFiles.when(() -> Files.exists(any(Path.class))).thenReturn(true);

            mockedFiles.when(() -> Files.write(imagePath, new byte[]{})).thenReturn(imagePath);

            when(jdbcTemplate.update(anyString(), anyString(),anyString(),anyString(),anyString(), anyInt(),anyInt())).thenReturn(1);

            imagemGateway.atualizar(imagem);

            verify(jdbcTemplate, times(1)).update(anyString(), anyString(),anyString(),anyString(),anyString(), anyInt(),anyInt());
       }
    }

    @Test
    void testEditarArquivoQuandoErroNaEscrita()  {
        Imagem imagem = new Imagem(1, "imagem2.jpg", "Descrição 2", "image/jpeg", 3072, "/caminho/imagem2.jpg", new byte[0]);
        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            Path imagePath = Paths.get(IMAGENS, "arquivo.jpg");

            mockedFiles.when(() -> Files.exists(Paths.get(IMAGENS))).thenReturn(true);
            mockedFiles.when(() -> Files.exists(imagePath)).thenReturn(true);

            mockedFiles.when(() -> Files.write(imagePath, new byte[]{})).thenThrow(new NotFoundException("Erro de escrita"));

            NotFoundException exception = assertThrows(NotFoundException.class, () -> {
                imagemGateway.editarArquivo(imagem);
            });

            assertEquals("Arquivo não existe", exception.getMessage());
        }
    }

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

    @Test
    void NaodeveExcluirImagemQuandoPathForInvalido() {
        Imagem imagem = new Imagem(1, "imagem2.jpg", "Descrição 2", "image/jpeg", 3072, "/caminho/imagem2.jpg", new byte[0]);

        when(jdbcTemplate.update(anyString(), anyInt())).thenReturn(1);

        imagemGateway.excluir(imagem);

        verify(jdbcTemplate, times(1)).update(eq("DELETE FROM imagem WHERE id=?"), eq(imagem.getId()));

        Path imagePath = Paths.get("imagens", "/caminho/imagem2.jpg");
        assertFalse(Files.exists(imagePath));
    }

    @Test
    void deveExcluirImagemComErroAoDeletarArquivo() {

        Imagem imagem = new Imagem(1, "imagem2.jpg", "Descrição 2", "image/jpeg", 3072, "/caminho/imagem2.jpg", new byte[0]);

        MockedStatic<Files> filesMocked = mockStatic(Files.class);
        filesMocked.when(() -> Files.deleteIfExists(any(Path.class))).thenThrow(new IOException("Erro ao deletar arquivo."));
        when(jdbcTemplate.update(anyString(), anyInt())).thenReturn(1);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            imagemGateway.excluir(imagem);
        });

        verify(jdbcTemplate, times(1)).update(eq("DELETE FROM imagem WHERE id=?"), eq(imagem.getId()));

        assertEquals("Erro ao deletar arquivo.", exception.getMessage());

        filesMocked.close();
    }



    @Test
    void deveLerArquivo() throws IOException {
        String path = "imagem.jpg";
        Path imagePath = Paths.get(IMAGENS + '/' + path);
        byte[] expectedContent = { 1, 2, 3, 4, 5 }; // Conteúdo do arquivo esperado

        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.readAllBytes(imagePath)).thenReturn(expectedContent);

            byte[] fileContent = imagemGateway.lerArquivo(path);

            assertArrayEquals(expectedContent, fileContent);
        }
    }

    @Test
    void deveDarErroAoLerArquivo() throws IOException {
        String path = "imagem_inexistente.jpg";
        Path imagePath = Paths.get(IMAGENS + '/' + path);

        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.readAllBytes(imagePath)).thenThrow(new IOException("Arquivo não encontrado"));

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                imagemGateway.lerArquivo(path);
            });

            assertEquals("Arquivo não encontrado.", exception.getMessage());
        }
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
