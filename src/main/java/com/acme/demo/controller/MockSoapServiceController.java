package com.acme.demo.controller;


import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.acme.demo.client.model.EnvioPedidoAcme;
import com.acme.demo.client.model.EnvioPedidoAcmeResponse;
import com.acme.demo.client.model.EnvioPedidoResponse;
import com.acme.demo.client.model.SoapBodyResponse;
import com.acme.demo.client.model.SoapEnvelope;
import com.acme.demo.client.model.SoapEnvelopeResponse;
import com.acme.demo.client.model.SoapHeader;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/mock")
public class MockSoapServiceController {

 @PostMapping(value = "/envio-pedidos",
              consumes = MediaType.APPLICATION_XML_VALUE,
              produces = MediaType.APPLICATION_XML_VALUE)
 public ResponseEntity<SoapEnvelopeResponse> recibirPedido(
         @RequestBody SoapEnvelope request,
         @RequestParam(required = false, defaultValue = "0") long delaySeconds) {
     
     log.info("=== MOCK SOAP SERVICE ===");
     log.info("Recibido request XML: {}", request);
     
     // Simular timeout/delay si se especifica
     if (delaySeconds > 0) {
         log.info("Aplicando delay de {} segundos...", delaySeconds);
         try {
             Thread.sleep(delaySeconds * 1000);
         } catch (InterruptedException e) {
             Thread.currentThread().interrupt();
             log.error("Delay interrumpido", e);
         }
     }
     
     // Validar que el request tenga datos
     if (request == null || 
         request.getBody() == null || 
         request.getBody().getEnvioPedidoAcme() == null) {
         log.error("Request inválido");
         return ResponseEntity.badRequest().build();
     }
     
     EnvioPedidoAcme pedidoAcme = request.getBody().getEnvioPedidoAcme();
     
     // Obtener el primer pedido
     if (pedidoAcme.getEnvioPedidoRequest() == null || 
         pedidoAcme.getEnvioPedidoRequest().isEmpty()) {
         log.error("No hay pedidos en el request");
         return ResponseEntity.badRequest().build();
     }
     
     // Crear la respuesta con los datos exactos del XML
     EnvioPedidoResponse response = EnvioPedidoResponse.builder()
         .codigo("80375472")
         .mensaje("Entregado exitosamente al cliente")
         .build();
     
     EnvioPedidoAcmeResponse acmeResponse = EnvioPedidoAcmeResponse.builder()
             .envioPedidoResponse(response)
             .build();
     
     SoapEnvelopeResponse envelopeResponse = SoapEnvelopeResponse.builder()
             .header(SoapHeader.builder().build())
             .body(SoapBodyResponse.builder()
                 .envioPedidoAcmeResponse(acmeResponse)
                 .build())
             .build();
     
     log.info("Enviando respuesta: Código=80375472, Mensaje=Entregado exitosamente al cliente");
     log.info("========================");
     
     return ResponseEntity.ok(envelopeResponse);
 }
 
 /**
  * Endpoint de health check para el mock
  */
 @GetMapping("/health")
 public ResponseEntity<String> health() {
     return ResponseEntity.ok("Mock SOAP Service is running!");
 }
}