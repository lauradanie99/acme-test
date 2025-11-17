package com.acme.demo.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.acme.demo.client.model.EnvioPedidoAcme;
import com.acme.demo.client.model.EnvioPedidoAcmeResponse;
import com.acme.demo.client.model.EnvioPedidoRequest;
import com.acme.demo.client.model.EnvioPedidoResponse;
import com.acme.demo.client.model.SoapBodyResponse;
import com.acme.demo.client.model.SoapEnvelopeResponse;
import com.acme.demo.client.model.SoapHeader;
import com.acme.demo.model.EnviarPedidoDTO;
import com.acme.demo.model.ResponseEnviarPedidoDTO;

public class EnvioPedidoUtilsTest {

	@Test
    @DisplayName("Debe mapear correctamente un JSON request válido a XML")
    void debeMapearJsonAXmlCorrectamente() {
        // Arrange
		EnviarPedidoDTO jsonRequest = EnviarPedidoDTO.builder()
            .numPedido("75630275")
            .cantidadPedido("1")
            .codigoEAN("00110000765191002104587")
            .nombreProducto("Armario INVAL")
            .numDocumento("1113987400")
            .direccion("CR 72B 45 12 APT 301")
            .build();


        // Act
        EnvioPedidoAcme result = EnvioPedidoUtils.jsonToXml(jsonRequest);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getEnvioPedidoRequest());
        assertFalse(result.getEnvioPedidoRequest().isEmpty());
        
        EnvioPedidoRequest xmlRequest = result.getEnvioPedidoRequest().get(0);
        assertEquals("75630275", xmlRequest.getPedido());
        assertEquals("1", xmlRequest.getCantidad());
        assertEquals("00110000765191002104587", xmlRequest.getEan());
        assertEquals("Armario INVAL", xmlRequest.getProducto());
        assertEquals("1113987400", xmlRequest.getCedula());
        assertEquals("CR 72B 45 12 APT 301", xmlRequest.getDireccion());
    }


    @Test
    @DisplayName("Debe lanzar IllegalArgumentException cuando jsonRequest es nulo")
    void debeLanzarExcepcionCuandoJsonRequestEsNulo() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> EnvioPedidoUtils.jsonToXml(null)
        );
        
        assertEquals("El request JSON no puede ser nulo", exception.getMessage());
    }

    @Test
    @DisplayName("Debe mapear correctamente campos vacíos")
    void debeMapearCamposVaciosJsonXml() {
        // Arrange
    	EnviarPedidoDTO jsonRequest = EnviarPedidoDTO.builder()
            .numPedido("")
            .cantidadPedido("")
            .codigoEAN("")
            .nombreProducto("")
            .numDocumento("")
            .direccion("")
            .build();

        // Act
        EnvioPedidoAcme result = EnvioPedidoUtils.jsonToXml(jsonRequest);

        // Assert
        assertNotNull(result);
        EnvioPedidoRequest xmlRequest = result.getEnvioPedidoRequest().get(0);
        assertEquals("", xmlRequest.getPedido());
        assertEquals("", xmlRequest.getEan());
        assertEquals("", xmlRequest.getProducto());
        assertEquals("", xmlRequest.getCedula());
        assertEquals("", xmlRequest.getDireccion());
    }
    
    @Test
    @DisplayName("Debe mapear correctamente un XML response válido a JSON")
    void debeMapearXmlAJsonCorrectamente() {
        // Arrange
    	EnvioPedidoResponse xmlResponse = EnvioPedidoResponse.builder()
            .codigo("80375472")
            .mensaje("Entregado exitosamente al cliente")
            .build();

        EnvioPedidoAcmeResponse acmeResponse = EnvioPedidoAcmeResponse.builder()
            .envioPedidoResponse(xmlResponse)
            .build();

        SoapBodyResponse bodyResponse = SoapBodyResponse.builder()
            .envioPedidoAcmeResponse(acmeResponse)
            .build();

        SoapEnvelopeResponse envelopeResponse = SoapEnvelopeResponse.builder()
            .header(SoapHeader.builder().build())
            .body(bodyResponse)
            .build();

        // Act
        ResponseEnviarPedidoDTO result = EnvioPedidoUtils.xmlToJson(envelopeResponse);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getEnviarPedidoRespuesta());
        assertEquals("80375472", result.getEnviarPedidoRespuesta().getCodigoEnvio());
        assertEquals("Entregado exitosamente al cliente", result.getEnviarPedidoRespuesta().getEstado());
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException cuando xmlResponse es nulo")
    void debeLanzarExcepcionCuandoXmlResponseEsNulo() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> EnvioPedidoUtils.xmlToJson(null)
        );
        
        assertEquals("La respuesta XML no puede ser nula", exception.getMessage());
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException cuando body es nulo")
    void debeLanzarExcepcionCuandoBodyEsNulo() {
        // Arrange
        SoapEnvelopeResponse envelopeResponse = SoapEnvelopeResponse.builder()
            .header(SoapHeader.builder().build())
            .body(null)
            .build();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> EnvioPedidoUtils.xmlToJson(envelopeResponse)
        );
        
        assertEquals("La respuesta XML no puede ser nula", exception.getMessage());
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException cuando envioPedidoAcmeResponse es nulo")
    void debeLanzarExcepcionCuandoAcmeResponseEsNulo() {
        // Arrange
        SoapBodyResponse bodyResponse = SoapBodyResponse.builder()
            .envioPedidoAcmeResponse(null)
            .build();

        SoapEnvelopeResponse envelopeResponse = SoapEnvelopeResponse.builder()
            .header(SoapHeader.builder().build())
            .body(bodyResponse)
            .build();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> EnvioPedidoUtils.xmlToJson(envelopeResponse)
        );
        
        assertEquals("La respuesta XML no puede ser nula", exception.getMessage());
    }
//
    @Test
    @DisplayName("Debe lanzar IllegalArgumentException cuando envioPedidoResponse es nulo")
    void debeLanzarExcepcionCuandoEnvioPedidoResponseEsNulo() {
        // Arrange
        EnvioPedidoAcmeResponse acmeResponse = EnvioPedidoAcmeResponse.builder()
            .envioPedidoResponse(null)
            .build();

        SoapBodyResponse bodyResponse = SoapBodyResponse.builder()
            .envioPedidoAcmeResponse(acmeResponse)
            .build();

        SoapEnvelopeResponse envelopeResponse = SoapEnvelopeResponse.builder()
            .header(SoapHeader.builder().build())
            .body(bodyResponse)
            .build();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> EnvioPedidoUtils.xmlToJson(envelopeResponse)
        );
        
        assertEquals("La respuesta XML no contiene datos", exception.getMessage());
    }

    @Test
    @DisplayName("Debe mapear correctamente códigos y mensajes vacíos")
    void debeMapearCamposVaciosXmlJson() {
        // Arrange
        EnvioPedidoResponse xmlResponse = EnvioPedidoResponse.builder()
            .codigo("")
            .mensaje("")
            .build();

        EnvioPedidoAcmeResponse acmeResponse = EnvioPedidoAcmeResponse.builder()
            .envioPedidoResponse(xmlResponse)
            .build();

        SoapBodyResponse bodyResponse = SoapBodyResponse.builder()
            .envioPedidoAcmeResponse(acmeResponse)
            .build();

        SoapEnvelopeResponse envelopeResponse = SoapEnvelopeResponse.builder()
            .header(SoapHeader.builder().build())
            .body(bodyResponse)
            .build();

        // Act
        ResponseEnviarPedidoDTO result = EnvioPedidoUtils.xmlToJson(envelopeResponse);

        // Assert
        assertNotNull(result);
        assertEquals("", result.getEnviarPedidoRespuesta().getCodigoEnvio());
        assertEquals("", result.getEnviarPedidoRespuesta().getEstado());
    }

    @Test
    @DisplayName("Debe mapear correctamente respuestas con diferentes códigos y mensajes")
    void debeMapearDiferentesCodigos() {
        // Arrange
        EnvioPedidoResponse xmlResponse = EnvioPedidoResponse.builder()
            .codigo("12345678")
            .mensaje("Pedido en tránsito")
            .build();

        EnvioPedidoAcmeResponse acmeResponse = EnvioPedidoAcmeResponse.builder()
            .envioPedidoResponse(xmlResponse)
            .build();

        SoapBodyResponse bodyResponse = SoapBodyResponse.builder()
            .envioPedidoAcmeResponse(acmeResponse)
            .build();

        SoapEnvelopeResponse envelopeResponse = SoapEnvelopeResponse.builder()
            .header(SoapHeader.builder().build())
            .body(bodyResponse)
            .build();

        // Act
        ResponseEnviarPedidoDTO result = EnvioPedidoUtils.xmlToJson(envelopeResponse);

        // Assert
        assertEquals("12345678", result.getEnviarPedidoRespuesta().getCodigoEnvio());
        assertEquals("Pedido en tránsito", result.getEnviarPedidoRespuesta().getEstado());
    }
}
