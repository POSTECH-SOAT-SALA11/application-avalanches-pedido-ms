package com.avalanches;

import com.avalanches.enterprisebusinessrules.entities.CategoriaProduto;
import com.avalanches.enterprisebusinessrules.entities.Imagem;
import com.avalanches.enterprisebusinessrules.entities.Produto;
import com.avalanches.frameworksanddrivers.api.dto.ImagemParams;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;

public class ProdutoBuilder {
    public static @NotNull Produto getProduto() {

        var imagens = getImagens();


        return new Produto(1,
                new BigDecimal("10.00"),
                10,
                CategoriaProduto.LANCHE,
                "XAvalanche",
                "XAvalanche com molho especial",
                imagens
        );
    }

    public static @NotNull ArrayList<Imagem> getImagens() {

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


    public static @NotNull ImagemParams[] getImagemParams() {

        var imagem = new ImagemParams(1,
                "xAvalanche.jpg",
                "xAvalanche1",
                "jpg",
                100,
                new byte[] {69, 121, 101, 45, 62, 118, 101, 114, (byte) 196, (byte) 195, 61, 101, 98}
        );

        return new ImagemParams[]{imagem};
    }
}
