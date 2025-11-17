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
public class SoapBody {
    
    @XmlElement(name = "EnvioPedidoAcme", namespace = "http://WSDLs/EnvioPedidos/EnvioPedidosAcme")
    private EnvioPedidoAcme envioPedidoAcme;
}
