package com.avalanches.interfaceadapters.controllers.interfaces;

import com.avalanches.enterprisebusinessrules.entities.Pedido;
import com.avalanches.enterprisebusinessrules.entities.StatusPedido;
import com.avalanches.frameworksanddrivers.databases.interfaces.BancoDeDadosContextoInterface;
import com.avalanches.interfaceadapters.presenters.dtos.PedidoDto;

import java.util.List;

public interface PedidoControllerInterface {

    Integer cadastrar(Pedido pedido, BancoDeDadosContextoInterface bancoDeDadosContexto);

    void atualizaStatus(Integer idPedido, StatusPedido statusPedido, BancoDeDadosContextoInterface bancoDeDadosContexto);

    List<PedidoDto> listar(BancoDeDadosContextoInterface bancoDeDadosContexto);

}
