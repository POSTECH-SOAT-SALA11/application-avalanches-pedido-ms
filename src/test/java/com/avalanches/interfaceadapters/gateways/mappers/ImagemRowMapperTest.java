package com.avalanches.interfaceadapters.gateways.mappers;

import com.avalanches.interfaceadapters.gateways.mapper.ImagemRowMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class ImagemRowMapperTest {

    ImagemRowMapper imagemRowMapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        imagemRowMapper = new ImagemRowMapper();
    }


    @Test
    void testProdutoRowMapper_Sucesso() throws SQLException {

        // Cria o mock do ResultSet
        ResultSet rs = mock(ResultSet.class);

        when(rs.getInt("id")).thenReturn(1);
        when(rs.getString("nome")).thenReturn("XAvalanche.jpg");
        when(rs.getString("descricao")).thenReturn("XAvalanche.jpg");
        when(rs.getString("tipoConteudo")).thenReturn("jpg");
        when(rs.getInt("tamanho")).thenReturn(10);
        when(rs.getString("caminho")).thenReturn("C:/Imagens");


        imagemRowMapper.mapRow(rs, 0);

        // Verifica se os métodos esperados foram chamados
        verify(rs, times(1)).getInt("id");
        verify(rs, times(1)).getString("nome");
    }
}
