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
public class EnviarPedidoRespuestaDTO {

	@JsonProperty("codigoEnvio")
    private String codigoEnvio;
    
    @JsonProperty("estado")
    private String estado;
}
