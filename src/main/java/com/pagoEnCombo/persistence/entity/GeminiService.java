package com.pagoEnCombo.persistence.entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GeminiService {

    @Value("${google.api.key}")
    private String googleApiKey;

    // Ahora el método está dentro de la clase
    public String getUrlApi() {
        String baseUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-flash-latest:generateContent";
        String apiKey =googleApiKey.trim();
        return baseUrl + "?key=" + apiKey;
    }

    public void llamarIA() {
        String urlFinal = getUrlApi();
        System.out.println("Conectando a: " + urlFinal);
        // Aquí va tu lógica de RestTemplate
    }

}
