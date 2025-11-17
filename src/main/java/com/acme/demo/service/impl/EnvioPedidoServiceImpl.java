package com.acme.demo.service.impl;

import org.springframework.stereotype.Service;

import com.acme.demo.client.EnvioPedidoApiClient;
import com.acme.demo.client.model.EnvioPedidoAcme;
import com.acme.demo.client.model.SoapEnvelopeResponse;
import com.acme.demo.model.RequestEnviarPedidoDTO;
import com.acme.demo.model.ResponseEnviarPedidoDTO;
import com.acme.demo.service.EnvioPedidoService;
import com.acme.demo.utils.EnvioPedidoUtils;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EnvioPedidoServiceImpl implements EnvioPedidoService {

	private final EnvioPedidoApiClient envioClient;

	@Override
	public ResponseEnviarPedidoDTO enviarPedido(RequestEnviarPedidoDTO requestPedido) {

		EnvioPedidoAcme xmlRequest = EnvioPedidoUtils.jsonToXml(requestPedido.getEnviarPedido());
		SoapEnvelopeResponse respuestaClient = envioClient.enviarPedido(xmlRequest);
		return EnvioPedidoUtils.xmlToJson(respuestaClient);
	}

}
