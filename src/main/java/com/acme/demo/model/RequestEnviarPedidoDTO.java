package com.acme.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestEnviarPedidoDTO {

	@JsonProperty("enviarPedido")
    private EnviarPedidoDTO enviarPedido;
}
