package com.avalanches.interfaceadapters.gateways;

import jakarta.validation.constraints.AssertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

public class PagamentoGatewayTest {

    @Mock
    RestTemplate restTemplate;

    @Spy
    @InjectMocks
    PagamentoGateway pagamentoGateway;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testEfetuarPagamento() {

        String pagamentoUrl = "http://k8s-default-ingressa-0faf251d7e-1124737897.sa-east-1.elb.amazonaws.com/avalanches/v1/pagamento/efetuar-pagamento/{idPedido}";
        int idPedido = 10;
        String urlFinal = pagamentoUrl.replace("{idPedido}", Integer.toString(idPedido));

        doReturn(restTemplate).when(pagamentoGateway).getRestTemplate();

        when(restTemplate.getForObject(urlFinal, Boolean.class)).thenReturn(true);

        Boolean pagamentoRealizado = pagamentoGateway.efetuarPagamento(10);

        assertThat(pagamentoRealizado).isTrue();
    }

}
