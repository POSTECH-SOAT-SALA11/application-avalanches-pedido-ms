Feature: Atualização de Status de Pedido

  Scenario: Atualizar Status de Pedido
    Given uma requisição com idPedido 123 e status "Pronto" é feita
    Then a requisição será processada com sucesso