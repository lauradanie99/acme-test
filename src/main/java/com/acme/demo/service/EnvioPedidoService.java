package com.acme.demo.service;

import com.acme.demo.model.RequestEnviarPedidoDTO;
import com.acme.demo.model.ResponseEnviarPedidoDTO;

public interface EnvioPedidoService {

	ResponseEnviarPedidoDTO enviarPedido(RequestEnviarPedidoDTO requestPedido);

}
