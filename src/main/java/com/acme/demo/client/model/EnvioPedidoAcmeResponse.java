package com.acme.demo.client.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
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
@XmlType(name = "EnvioPedidoAcmeResponse", propOrder = {
    "envioPedidoResponse"
})
@XmlRootElement(name = "EnvioPedidoAcmeResponse", namespace = "http://WSDLs/EnvioPedidos/EnvioPedidosAcme")
public class EnvioPedidoAcmeResponse {

    @XmlElement(name = "EnvioPedidoResponse", required = true)
    private EnvioPedidoResponse envioPedidoResponse;
}
