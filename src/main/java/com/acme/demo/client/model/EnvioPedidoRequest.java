package com.acme.demo.client.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.xml.bind.annotation.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EnvioPedidoRequest", propOrder = {
    "pedido",
    "cantidad",
    "ean",
    "producto",
    "cedula",
    "direccion"
})
public class EnvioPedidoRequest {

    @XmlElement(required = false)
    private String pedido;

    @XmlElement(name = "Cantidad", required = false)
    private String cantidad;

    @XmlElement(name = "EAN", required = false)
    private String ean;

    @XmlElement(name = "Producto", required = false)
    private String producto;

    @XmlElement(name = "Cedula", required = false)
    private String cedula;

    @XmlElement(name = "Direccion", required = false)
    private String direccion;
}
