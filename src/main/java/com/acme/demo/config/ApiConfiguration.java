package com.acme.demo.config;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Collections;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApiConfiguration {

	@Bean
    RestTemplate restTemplate() throws NoSuchAlgorithmException, KeyManagementException {
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());
        
        Jaxb2RootElementHttpMessageConverter xmlConverter = new Jaxb2RootElementHttpMessageConverter();
        restTemplate.setMessageConverters(Collections.singletonList(xmlConverter));
        
        return restTemplate;
    }

    @Bean
    ClientHttpRequestFactory clientHttpRequestFactory() 
            throws NoSuchAlgorithmException, KeyManagementException {
        
        // Deshabilitar validación SSL, se debe quitar cuando el servicio tenga certificados validos
        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
        };

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
        
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(10000);
        
        return factory;
    }
}

