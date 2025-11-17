package com.acme.demo.client.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@XmlAccessorType(XmlAccessType.FIELD)
public class SoapHeader {
    // Clase vacía porque no tiene headers, sin embargo es necesaria para generar
	// el request y el response del ejemplo
}
