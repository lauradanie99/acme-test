# acme-test

## Descripción
API para consumo de servicio acme de envío de pedidos

## Tecnologías
- Spring Boot 3.5.7
- Java 21
- Maven
- Docker (para despliegue)

## Requisitos
- JDK 21
- Maven 3.9+
- Docker (para despliegue)

## Cómo ejecutar en local
```bash
mvn clean install
mvn spring-boot:run
```

## Endpoints principales

POST /api/acme/pedidos/enviar - para envío de pedidos al servicio de acme

## Cómo ejecutar con Docker
```bash
docker build -t acme-test .
docker run -p 8080:8080 acme-test
```

## Acceso a swagger
Una vez arrancada la app:

[Swagger](http://localhost:8080/api/acme/swagger-ui/index.html)
[API-docs](http://localhost:8080/api/acme/api-docs)

### Notas:
Ya que actualmente está arrojando un error de certificados y de NOT_FOUND, se hicieron ajustes en código. Para el error de certificados, en la clase de configuración de ApiConfiguration se agregó código para deshabilitar la validación de certificados, pero debe quitarse. Para el error de NOT_FOUND se hizo un endpoint local para poder probar el llamado del servicio; si se quiere probar con el endpoint local, se debe configurar soap.service.url en http://localhost:8080/api/acme/mock/envio-pedidos