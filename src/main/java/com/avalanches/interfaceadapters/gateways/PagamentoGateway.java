package com.avalanches.interfaceadapters.gateways;

import com.avalanches.interfaceadapters.controllers.interfaces.PagamentoGatewayInterface;
import org.springframework.web.client.RestTemplate;

public class PagamentoGateway implements PagamentoGatewayInterface {

    private static final String pagamentoUrl = "http://k8s-default-ingressa-0faf251d7e-1124737897.sa-east-1.elb.amazonaws.com/avalanches/v1/pagamento/efetuar-pagamento/{idPedido}";

    @Override
    public Boolean efetuarPagamento(Integer idPedido) {

        String url = pagamentoUrl.replace("{idPedido}",idPedido.toString());
        RestTemplate restTemplate = getRestTemplate();

        return restTemplate.getForObject(url, Boolean.class);
    }

    protected RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
