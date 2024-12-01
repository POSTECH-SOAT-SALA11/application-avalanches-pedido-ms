package com.avalanches.interfaceadapters.gateways.mappers;

import com.avalanches.interfaceadapters.gateways.mapper.ProdutoRowMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ProdutoRowMapperTest {


    ProdutoRowMapper produtoRowMapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        produtoRowMapper = new ProdutoRowMapper();
    }


    @Test
    void testProdutoRowMapper_Sucesso() throws SQLException {

        // Cria o mock do ResultSet
        ResultSet rs = mock(ResultSet.class);

        when(rs.getInt("id")).thenReturn(1);
        when(rs.getBigDecimal("valor")).thenReturn(new BigDecimal("10"));
        when(rs.getInt("quantidade")).thenReturn(10);
        when(rs.getString("categoria")).thenReturn("lanche");
        when(rs.getString("nome")).thenReturn("XAvalanche");
        when(rs.getString("descricao")).thenReturn("XAvalanche");

        produtoRowMapper.mapRow(rs, 0);

        // Verifica se os métodos esperados foram chamados
        verify(rs, times(1)).getInt("id");
        verify(rs, times(1)).getString("nome");
    }

    @Test
    void testProdutoRowMapper_CategoriaDesconhecida() throws SQLException {

        // Cria o mock do ResultSet
        ResultSet rs = mock(ResultSet.class);

        when(rs.getInt("id")).thenReturn(1);
        when(rs.getBigDecimal("valor")).thenReturn(new BigDecimal("10"));
        when(rs.getInt("quantidade")).thenReturn(10);
        when(rs.getString("categoria")).thenReturn("lanche123");
        when(rs.getString("nome")).thenReturn("XAvalanche");
        when(rs.getString("descricao")).thenReturn("XAvalanche");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            produtoRowMapper.mapRow(rs,0);
        });

        assertEquals("Categoria desconhecida: lanche123", exception.getMessage());
    }
}
