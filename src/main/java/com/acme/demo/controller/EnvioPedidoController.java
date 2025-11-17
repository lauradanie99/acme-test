package com.acme.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acme.demo.model.RequestEnviarPedidoDTO;
import com.acme.demo.model.ResponseEnviarPedidoDTO;
import com.acme.demo.service.EnvioPedidoService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/pedidos")
@AllArgsConstructor
public class EnvioPedidoController {

	private final EnvioPedidoService envioService;
	
	@PostMapping("/enviar")
	public ResponseEntity<ResponseEnviarPedidoDTO> enviarPedido(@RequestBody RequestEnviarPedidoDTO requestPedido){
		
		ResponseEnviarPedidoDTO response = envioService.enviarPedido(requestPedido);
		return ResponseEntity.ok(response);
		
	}
	
}
