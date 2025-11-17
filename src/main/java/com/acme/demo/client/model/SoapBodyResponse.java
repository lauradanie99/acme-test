package com.acme.demo.client.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class SoapBodyResponse {
    
    @XmlElement(name = "EnvioPedidoAcmeResponse", namespace = "http://WSDLs/EnvioPedidos/EnvioPedidosAcme")
    private EnvioPedidoAcmeResponse envioPedidoAcmeResponse;
}