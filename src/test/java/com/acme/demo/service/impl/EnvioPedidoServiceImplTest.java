package com.acme.demo.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.acme.demo.client.EnvioPedidoApiClient;
import com.acme.demo.client.model.EnvioPedidoAcme;
import com.acme.demo.client.model.EnvioPedidoAcmeResponse;
import com.acme.demo.client.model.EnvioPedidoRequest;
import com.acme.demo.client.model.EnvioPedidoResponse;
import com.acme.demo.client.model.SoapBodyResponse;
import com.acme.demo.client.model.SoapEnvelopeResponse;
import com.acme.demo.client.model.SoapHeader;
import com.acme.demo.controller.advicer.ApiException;
import com.acme.demo.model.EnviarPedidoDTO;
import com.acme.demo.model.RequestEnviarPedidoDTO;
import com.acme.demo.model.ResponseEnviarPedidoDTO;

@ExtendWith(MockitoExtension.class)
class EnvioPedidoServiceImplTest {

    @Mock
    private EnvioPedidoApiClient envioClient;

    @InjectMocks
    private EnvioPedidoServiceImpl service;

    private RequestEnviarPedidoDTO requestPedidoDTO;
    private SoapEnvelopeResponse soapEnvelopeResponse;

    @BeforeEach
    void setUp() {
        // Arrange - Request JSON
    	EnviarPedidoDTO jsonRequest = EnviarPedidoDTO.builder()
                .numPedido("75630275")
                .cantidadPedido("1")
                .codigoEAN("00110000765191002104587")
                .nombreProducto("Armario INVAL")
                .numDocumento("1113987400")
                .direccion("CR 72B 45 12 APT 301")
                .build();

        requestPedidoDTO = RequestEnviarPedidoDTO.builder()
            .enviarPedido(jsonRequest)
            .build();

        // Arrange - Response XML
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

        soapEnvelopeResponse = SoapEnvelopeResponse.builder()
            .header(SoapHeader.builder().build())
            .body(bodyResponse)
            .build();
    }

    @Test
    @DisplayName("Debe enviar pedido exitosamente y retornar respuesta mapeada")
    void debeEnviarPedidoExitosamente() {
        // Arrange
        when(envioClient.enviarPedido(any(EnvioPedidoAcme.class)))
            .thenReturn(soapEnvelopeResponse);

        // Act
        ResponseEnviarPedidoDTO result = service.enviarPedido(requestPedidoDTO);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getEnviarPedidoRespuesta());
        assertEquals("80375472", result.getEnviarPedidoRespuesta().getCodigoEnvio());
        assertEquals("Entregado exitosamente al cliente", result.getEnviarPedidoRespuesta().getEstado());

        // Verificar que el cliente fue llamado exactamente una vez
        verify(envioClient, times(1)).enviarPedido(any(EnvioPedidoAcme.class));
    }

    @Test
    @DisplayName("Debe llamar al cliente con los datos correctos del request")
    void debeLlamarClienteConDatosCorrectos() {
        // Arrange
        when(envioClient.enviarPedido(any(EnvioPedidoAcme.class)))
            .thenReturn(soapEnvelopeResponse);

        // Act
        service.enviarPedido(requestPedidoDTO);

        // Assert
        verify(envioClient).enviarPedido(argThat(xmlRequest -> {
            assertNotNull(xmlRequest);
            assertNotNull(xmlRequest.getEnvioPedidoRequest());
            assertFalse(xmlRequest.getEnvioPedidoRequest().isEmpty());
            
            EnvioPedidoRequest request = xmlRequest.getEnvioPedidoRequest().get(0);
            assertEquals("75630275", request.getPedido());
            assertEquals("1", request.getCantidad());
            assertEquals("00110000765191002104587", request.getEan());
            assertEquals("Armario INVAL", request.getProducto());
            assertEquals("1113987400", request.getCedula());
            assertEquals("CR 72B 45 12 APT 301", request.getDireccion());
            
            return true;
        }));
    }

    @Test
    @DisplayName("Debe propagar ApiException cuando el cliente lanza excepción")
    void debePropagrarApiException() {
        // Arrange
        ApiException apiException = new ApiException("Error en el servicio", 500);
        when(envioClient.enviarPedido(any(EnvioPedidoAcme.class)))
            .thenThrow(apiException);

        // Act & Assert
        ApiException exception = assertThrows(
            ApiException.class,
            () -> service.enviarPedido(requestPedidoDTO)
        );

        assertEquals("Error en el servicio", exception.getMessage());
        assertEquals(500, exception.getStatusCode());
        verify(envioClient, times(1)).enviarPedido(any(EnvioPedidoAcme.class));
    }

    @Test
    @DisplayName("Debe manejar respuesta con código diferente")
    void debeManejarRespuestaConCodigoDiferente() {
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

        SoapEnvelopeResponse customResponse = SoapEnvelopeResponse.builder()
            .header(SoapHeader.builder().build())
            .body(bodyResponse)
            .build();

        when(envioClient.enviarPedido(any(EnvioPedidoAcme.class)))
            .thenReturn(customResponse);

        // Act
        ResponseEnviarPedidoDTO result = service.enviarPedido(requestPedidoDTO);

        // Assert
        assertNotNull(result);
        assertEquals("12345678", result.getEnviarPedidoRespuesta().getCodigoEnvio());
        assertEquals("Pedido en tránsito", result.getEnviarPedidoRespuesta().getEstado());
    }

    @Test
    @DisplayName("Debe manejar request con campos vacíos")
    void debeManejarRequestConCamposVacios() {
        // Arrange
    	EnviarPedidoDTO pedidoData = EnviarPedidoDTO.builder()
            .numPedido("")
            .cantidadPedido("")
            .codigoEAN("")
            .nombreProducto("")
            .numDocumento("")
            .direccion("")
            .build();
        

        RequestEnviarPedidoDTO emptyRequest = RequestEnviarPedidoDTO.builder()
            .enviarPedido(pedidoData)
            .build();

        when(envioClient.enviarPedido(any(EnvioPedidoAcme.class)))
            .thenReturn(soapEnvelopeResponse);

        // Act
        ResponseEnviarPedidoDTO result = service.enviarPedido(emptyRequest);

        // Assert
        assertNotNull(result);
        verify(envioClient, times(1)).enviarPedido(any(EnvioPedidoAcme.class));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException cuando request es nulo")
    void debeLanzarExcepcionCuandoRequestEsNulo() {
        // Arrange
        RequestEnviarPedidoDTO nullRequest = RequestEnviarPedidoDTO.builder()
            .enviarPedido(null)
            .build();

        // Act & Assert
        assertThrows(
            IllegalArgumentException.class,
            () -> service.enviarPedido(nullRequest)
        );

        // Verificar que el cliente NO fue llamado
        verify(envioClient, never()).enviarPedido(any(EnvioPedidoAcme.class));
    }


    @Test
    @DisplayName("Debe manejar múltiples llamadas al servicio")
    void debeManejarMultiplesLlamadas() {
        // Arrange
        when(envioClient.enviarPedido(any(EnvioPedidoAcme.class)))
            .thenReturn(soapEnvelopeResponse);

        // Act
        ResponseEnviarPedidoDTO result1 = service.enviarPedido(requestPedidoDTO);
        ResponseEnviarPedidoDTO result2 = service.enviarPedido(requestPedidoDTO);
        ResponseEnviarPedidoDTO result3 = service.enviarPedido(requestPedidoDTO);

        // Assert
        assertNotNull(result1);
        assertNotNull(result2);
        assertNotNull(result3);
        
        // Verificar que el cliente fue llamado 3 veces
        verify(envioClient, times(3)).enviarPedido(any(EnvioPedidoAcme.class));
    }

    @Test
    @DisplayName("Debe manejar RuntimeException del cliente")
    void debeManejarRuntimeException() {
        // Arrange
        when(envioClient.enviarPedido(any(EnvioPedidoAcme.class)))
            .thenThrow(new RuntimeException("Error inesperado"));

        // Act & Assert
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> service.enviarPedido(requestPedidoDTO)
        );

        assertEquals("Error inesperado", exception.getMessage());
        verify(envioClient, times(1)).enviarPedido(any(EnvioPedidoAcme.class));
    }

    @Test
    @DisplayName("Debe manejar respuesta con campos nulos")
    void debeManejarRespuestaConCamposNulos() {
        // Arrange
        EnvioPedidoResponse xmlResponse = EnvioPedidoResponse.builder()
            .codigo(null)
            .mensaje(null)
            .build();

        EnvioPedidoAcmeResponse acmeResponse = EnvioPedidoAcmeResponse.builder()
            .envioPedidoResponse(xmlResponse)
            .build();

        SoapBodyResponse bodyResponse = SoapBodyResponse.builder()
            .envioPedidoAcmeResponse(acmeResponse)
            .build();

        SoapEnvelopeResponse nullFieldsResponse = SoapEnvelopeResponse.builder()
            .header(SoapHeader.builder().build())
            .body(bodyResponse)
            .build();

        when(envioClient.enviarPedido(any(EnvioPedidoAcme.class)))
            .thenReturn(nullFieldsResponse);

        // Act
        ResponseEnviarPedidoDTO result = service.enviarPedido(requestPedidoDTO);

        // Assert
        assertNotNull(result);
        assertNull(result.getEnviarPedidoRespuesta().getCodigoEnvio());
        assertNull(result.getEnviarPedidoRespuesta().getEstado());
    }
}