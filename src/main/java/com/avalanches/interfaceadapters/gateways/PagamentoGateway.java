package com.avalanches.interfaceadapters.gateways;

import com.avalanches.interfaceadapters.controllers.interfaces.PagamentoGatewayInterface;
import org.springframework.web.client.RestTemplate;

public class PagamentoGateway implements PagamentoGatewayInterface {

    private static final String pagamentoUrl = "https://307946636040.dkr.ecr.sa-east-1.amazonaws.com/ms-pagamento/avalanches/v1/pagamento/efetuar-pagamento/";

    @Override
    public Boolean efetuarPagamento(Integer idPedido) {

        String url = pagamentoUrl + idPedido;
        RestTemplate restTemplate = getRestTemplate();

        return restTemplate.getForObject(url, Boolean.class);
    }

    protected RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
