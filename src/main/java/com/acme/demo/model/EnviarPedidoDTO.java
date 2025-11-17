package com.acme.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EnviarPedidoDTO {

	@JsonProperty("numPedido")
    private String numPedido;
    
    @JsonProperty("cantidadPedido")
    private String cantidadPedido;
    
    @JsonProperty("codigoEAN")
    private String codigoEAN;
    
    @JsonProperty("nombreProducto")
    private String nombreProducto;
    
    @JsonProperty("numDocumento")
    private String numDocumento;
    
    @JsonProperty("direccion")
    private String direccion;
}
