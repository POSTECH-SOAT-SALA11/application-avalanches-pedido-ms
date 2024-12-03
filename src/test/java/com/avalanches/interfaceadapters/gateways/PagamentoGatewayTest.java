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

        String url = "https://307946636040.dkr.ecr.sa-east-1.amazonaws.com/ms-pagamento/avalanches/v1/pagamento/efetuar-pagamento/10";

        doReturn(restTemplate).when(pagamentoGateway).getRestTemplate();

        when(restTemplate.getForObject(url, Boolean.class)).thenReturn(true);

        Boolean pagamentoRealizado = pagamentoGateway.efetuarPagamento(10);

        assertThat(pagamentoRealizado).isTrue();
    }

}
