package com.acme.demo.utils;

import org.springframework.stereotype.Component;

import com.acme.demo.client.model.EnvioPedidoAcme;
import com.acme.demo.client.model.EnvioPedidoAcmeResponse;
import com.acme.demo.client.model.EnvioPedidoRequest;
import com.acme.demo.client.model.EnvioPedidoResponse;
import com.acme.demo.client.model.SoapEnvelopeResponse;
import com.acme.demo.model.EnviarPedidoDTO;
import com.acme.demo.model.EnviarPedidoRespuestaDTO;
import com.acme.demo.model.ResponseEnviarPedidoDTO;

@Component
public class EnvioPedidoUtils {

	public static EnvioPedidoAcme jsonToXml(EnviarPedidoDTO data) {

		EnvioPedidoRequest request = EnvioPedidoRequest.builder()
				.pedido(data.getNumPedido())
				.cantidad(data.getCantidadPedido())
				.ean(data.getCodigoEAN())
				.producto(data.getNombreProducto())
				.cedula(data.getNumDocumento())
				.direccion(data.getDireccion()).build();

		EnvioPedidoAcme xmlRequest = EnvioPedidoAcme.builder().build();
		xmlRequest.addEnvioPedidoRequest(request);

		return xmlRequest;
	}

	public static ResponseEnviarPedidoDTO xmlToJson(SoapEnvelopeResponse xmlResponse) {

		if (xmlResponse == null || 
				xmlResponse.getBody() == null || 
				xmlResponse.getBody().getEnvioPedidoAcmeResponse() == null) {
			throw new IllegalArgumentException("La respuesta XML no puede ser nula");
		}

		EnvioPedidoAcmeResponse acmeResponse = xmlResponse.getBody().getEnvioPedidoAcmeResponse();

		if (acmeResponse.getEnvioPedidoResponse() == null) {
			throw new IllegalArgumentException("La respuesta XML no contiene datos");
		}

		EnvioPedidoResponse response = acmeResponse.getEnvioPedidoResponse();

		EnviarPedidoRespuestaDTO respuestaData = EnviarPedidoRespuestaDTO.builder().codigoEnvio(response.getCodigo())
				.estado(response.getMensaje()).build();

		return ResponseEnviarPedidoDTO.builder().enviarPedidoRespuesta(respuestaData).build();
	}

}