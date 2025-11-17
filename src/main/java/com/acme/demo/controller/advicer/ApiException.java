package com.acme.demo.controller.advicer;


public class ApiException extends RuntimeException {
    
	private static final long serialVersionUID = 1L;
	private final int statusCode;
    private final String detalles;

    public ApiException(String mensaje) {
        super(mensaje);
        this.statusCode = 500;
        this.detalles = null;
    }

    public ApiException(String mensaje, String detalles) {
        super(mensaje);
        this.statusCode = 500;
        this.detalles = detalles;
    }

    public ApiException(String mensaje, int statusCode) {
        super(mensaje);
        this.statusCode = statusCode;
        this.detalles = null;
    }

    public ApiException(String mensaje, String detalles, int statusCode) {
        super(mensaje);
        this.statusCode = statusCode;
        this.detalles = detalles;
    }

    public ApiException(String mensaje, Throwable cause) {
        super(mensaje, cause);
        this.statusCode = 500;
        this.detalles = cause.getMessage();
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getDetalles() {
        return detalles;
    }

}
