package com.acme.demo.controller.advicer;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.acme.demo.model.ErrorResponse;

import jakarta.xml.bind.JAXBException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ApiExcpetionHandler {

	@ExceptionHandler(ApiException.class)
	public ResponseEntity<ErrorResponse> handleApiException(ApiException ex, WebRequest request) {

		log.error("Error en servicio: {}", ex.getMessage(), ex);

		HttpStatus status = HttpStatus.resolve(ex.getStatusCode());
		if (status == null) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		ErrorResponse error = ErrorResponse.builder().status(status.value()).error(status.name())
				.mensaje(ex.getMessage()).detalles(ex.getDetalles()).build();

		return new ResponseEntity<>(error, status);
	}

	/**
	 * Maneja errores de timeout
	 */
	@ExceptionHandler({ SocketTimeoutException.class, ResourceAccessException.class })
	public ResponseEntity<ErrorResponse> handleTimeoutException(Exception ex, WebRequest request) {

		log.error("Timeout al conectar con el servicio: {}", ex.getMessage(), ex);

		ErrorResponse error = ErrorResponse.builder().status(HttpStatus.REQUEST_TIMEOUT.value())
				.error("Request Timeout").mensaje("El servicio externo no respondió a tiempo")
				.detalles("Verifique que el servicio esté disponible o aumente el timeout configurado").build();

		return new ResponseEntity<>(error, HttpStatus.SERVICE_UNAVAILABLE);
	}

	/**
	 * Maneja errores de conexión
	 */
	@ExceptionHandler(ConnectException.class)
	public ResponseEntity<ErrorResponse> handleConnectException(ConnectException ex, WebRequest request) {

		log.error("Error de conexión: {}", ex.getMessage(), ex);

		ErrorResponse error = ErrorResponse.builder()
				.status(HttpStatus.SERVICE_UNAVAILABLE.value()).error("SERVICE_UNAVAILABLE")
				.mensaje("No se pudo conectar con el servicio externo")
				.detalles("Verifique que la URL del servicio sea correcta y que esté disponible").build();

		return new ResponseEntity<>(error, HttpStatus.SERVICE_UNAVAILABLE);
	}

	/**
	 * Maneja errores 4xx del servicio externo
	 */
	@ExceptionHandler(HttpClientErrorException.class)
	public ResponseEntity<ErrorResponse> handleHttpClientError(HttpClientErrorException ex, WebRequest request) {

		log.error("Error del cliente HTTP: {} - {}", ex.getStatusCode(), ex.getMessage());

		HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
		String errorName = status != null ? status.name() : "CLIENT_ERROR";

		ErrorResponse error = ErrorResponse.builder().status(ex.getStatusCode().value()).error(errorName)
				.mensaje("El servicio externo rechazó la petición").detalles(ex.getResponseBodyAsString()).build();

		return new ResponseEntity<>(error, ex.getStatusCode());
	}

	/**
	 * Maneja errores 5xx del servicio externo
	 */
	@ExceptionHandler(HttpServerErrorException.class)
	public ResponseEntity<ErrorResponse> handleHttpServerError(HttpServerErrorException ex, WebRequest request) {

		log.error("Error del servidor HTTP: {} - {}", ex.getStatusCode(), ex.getMessage());

		HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
		String errorName = status != null ? status.name() : "SERVER_ERROR";

		ErrorResponse error = ErrorResponse.builder().status(ex.getStatusCode().value()).error(errorName)
				.mensaje("El servicio externo experimentó un error interno").detalles(ex.getResponseBodyAsString())
				.build();

		return new ResponseEntity<>(error, ex.getStatusCode());
	}

	/**
	 * Maneja errores de parseo de XML/JSON
	 */
	@ExceptionHandler({ JAXBException.class, HttpMessageNotReadableException.class })
	public ResponseEntity<ErrorResponse> handleParseException(Exception ex, WebRequest request) {

		log.error("Error al parsear el mensaje: {}", ex.getMessage(), ex);

		ErrorResponse error = ErrorResponse.builder().status(HttpStatus.BAD_REQUEST.value()).error("BAD_REQUEST")
				.mensaje("Error al procesar el formato del mensaje")
				.detalles("Verifique que el formato del request sea válido (JSON o XML)").build();

		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Maneja validaciones de parámetros
	 */
	@ExceptionHandler({ MethodArgumentNotValidException.class, MissingServletRequestParameterException.class,
			MethodArgumentTypeMismatchException.class })
	public ResponseEntity<ErrorResponse> handleValidationException(Exception ex, WebRequest request) {

		log.error("Error de validación: {}", ex.getMessage());

		ErrorResponse error = ErrorResponse.builder().status(HttpStatus.BAD_REQUEST.value()).error("BAD_REQUEST")
				.mensaje("Error en los parámetros de entrada").detalles(ex.getMessage()).build();

		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Maneja IllegalArgumentException
	 */
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex,
			WebRequest request) {

		log.error("Argumento inválido: {}", ex.getMessage());

		ErrorResponse error = ErrorResponse.builder().status(HttpStatus.BAD_REQUEST.value()).error("BAD_REQUEST")
				.mensaje(ex.getMessage())
				.detalles("Verifique que todos los campos requeridos estén presentes y sean válidos").build();

		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Maneja cualquier otra excepción no capturada
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, WebRequest request) {

		log.error("Error inesperado: {}", ex.getMessage(), ex);

		ErrorResponse error = ErrorResponse.builder().status(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.error("INTERNAL_SERVER_ERROR").mensaje("Ocurrió un error inesperado en el servidor")
				.detalles(ex.getMessage()).build();

		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
