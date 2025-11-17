package com.acme.demo.client.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EnvioPedidoResponse", propOrder = {
    "codigo",
    "mensaje"
})
public class EnvioPedidoResponse {

    @XmlElement(name = "Codigo", required = false)
    private String codigo;

    @XmlElement(name = "Mensaje", required = false)
    private String mensaje;
}
