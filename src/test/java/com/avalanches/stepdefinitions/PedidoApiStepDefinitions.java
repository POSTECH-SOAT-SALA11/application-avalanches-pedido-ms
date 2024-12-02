package com.avalanches.stepdefinitions;

import com.avalanches.enterprisebusinessrules.entities.StatusPedido;
import com.avalanches.frameworksanddrivers.api.PedidoApi;
import com.avalanches.frameworksanddrivers.api.interfaces.PedidoApiInterface;
import com.avalanches.frameworksanddrivers.databases.interfaces.BancoDeDadosContextoInterface;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import jakarta.inject.Inject;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PedidoApiStepDefinitions {

    private PedidoApiInterface pedidoApi;
    private ResponseEntity<Void> response;

    @Given("uma requisição com idPedido {int} e status {string} é feita")
    public void uma_requisição_com_id_pedido_e_status_é_feita(int idPedido, String status) {
        // Write code here that turns the phrase above into concrete actions
        pedidoApi = mock(PedidoApi.class);

        StatusPedido statusPedido = StatusPedido.valueOf(status.toUpperCase());

        ResponseEntity<Void> responseEntity = ResponseEntity.ok().build();

        when(pedidoApi.atualizaStatus(anyInt(), any(StatusPedido.class))).thenReturn(responseEntity);

        response = pedidoApi.atualizaStatus(idPedido, statusPedido);
    }

    @Then("a requisição será processada com sucesso")
    public void a_requisição_será_processada_com_sucesso() {
        // Write code here that turns the phrase above into concrete actions
        assertTrue(response.getStatusCode() == HttpStatus.OK);
    }


}
