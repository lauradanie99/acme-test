package com.acme.demo.client.model;

import java.util.ArrayList;
import java.util.List;

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
@XmlType(name = "EnvioPedidoAcme", propOrder = { "envioPedidoRequest" })
@XmlRootElement(name = "EnvioPedidoAcme", namespace = "http://WSDLs/EnvioPedidos/EnvioPedidosAcme")
public class EnvioPedidoAcme {

	@XmlElement(name = "EnvioPedidoRequest", required = true)
	@Builder.Default
	private List<EnvioPedidoRequest> envioPedidoRequest = new ArrayList<>();

	public void addEnvioPedidoRequest(EnvioPedidoRequest request) {
		if (this.envioPedidoRequest == null) {
			this.envioPedidoRequest = new ArrayList<>();
		}
		this.envioPedidoRequest.add(request);
	}

}
