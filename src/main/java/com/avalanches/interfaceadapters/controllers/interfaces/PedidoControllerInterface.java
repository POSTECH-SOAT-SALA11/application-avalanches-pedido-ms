package com.avalanches.interfaceadapters.controllers.interfaces;

import com.avalanches.enterprisebusinessrules.entities.Pedido;
import com.avalanches.frameworksanddrivers.databases.interfaces.BancoDeDadosContextoInterface;
import com.avalanches.interfaceadapters.presenters.dtos.PedidoDto;

import java.util.List;

public interface PedidoControllerInterface {

    Integer cadastrar(Pedido pedido, BancoDeDadosContextoInterface bancoDeDadosContexto);

    List<PedidoDto> listar(BancoDeDadosContextoInterface bancoDeDadosContexto);

}
