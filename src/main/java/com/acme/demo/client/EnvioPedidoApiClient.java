package com.acme.demo.client;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.acme.demo.client.model.EnvioPedidoAcme;
import com.acme.demo.client.model.SoapBody;
import com.acme.demo.client.model.SoapEnvelope;
import com.acme.demo.client.model.SoapEnvelopeResponse;
import com.acme.demo.client.model.SoapHeader;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnvioPedidoApiClient {

	//TODO: si se tuviera un SOAP real, no se usa webclient sino jaxb2 y webservicetemplate
	
	private final RestTemplate restTemplate;
	
	@Value("${soap.service.url}")
    private String serviceUrl;

	public SoapEnvelopeResponse enviarPedido(EnvioPedidoAcme request) {
        try {
        	SoapEnvelope envelope = SoapEnvelope.builder()
                    .header(SoapHeader.builder().build())
                    .body(SoapBody.builder()
                        .envioPedidoAcme(request)
                        .build())
                    .build();

            log.info("Enviando pedido al servicio REST (XML): {}", request);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_XML);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_XML));

            HttpEntity<SoapEnvelope> entity = new HttpEntity<>(envelope, headers);

            ResponseEntity<SoapEnvelopeResponse> response = restTemplate.exchange(
                serviceUrl,
                HttpMethod.POST,
                entity,
                SoapEnvelopeResponse.class
            );

            log.info("Respuesta recibida. Status: {}", response.getStatusCode());
            log.debug("Body: {}", response.getBody());

            return response.getBody();

        } catch (Exception e) {
            log.error("Error al enviar pedido: ", e);
            throw new RuntimeException("Error en el servicio REST XML", e);
        }
    }
}

