package com.pagoEnCombo.persistence;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagoEnCombo.persistence.crud.PagoEnComboCrudRepository;
import com.pagoEnCombo.persistence.crud.UsuarioCrudRepository;
import com.pagoEnCombo.persistence.entity.PagoEnCombo;
import com.pagoEnCombo.persistence.entity.PagoEnComboResponse;
import com.pagoEnCombo.persistence.entity.RestTemplateConfig;
import com.pagoEnCombo.persistence.entity.Usuario;

import java.util.Map;
import java.util.List;

@Service
public class PagoEnComboRepository {

    @Autowired
    private PagoEnComboCrudRepository pagoEnComboCrudRepository;

    @Autowired
    private UsuarioCrudRepository usuarioCrudRepository;

    @Autowired
    private RestTemplate restTemplate;

    // 1. Define la URL con tu API Key de Google Gemini
    // Cambiamos a gemini-pro que suele estar habilitado por defecto
    // Cambiamos v1beta por v1 y nos aseguramos de que el nombre del modelo sea el base
    // Muchos proyectos nuevos están requiriendo la versión completa del nombre para evitar el 404
    // Opción Recomendada: Gemini 2.5 Flash-Lite (la más estable y rápida según tu JSON)
    private final String urlApi = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash-lite:generateContent?key=AIzaSyDxHCRN3q2SnRAqub26sF0YGwcdydVJiu0";

    public PagoEnCombo CapturarFactura(PagoEnCombo pagoEnCombo) {

        return pagoEnComboCrudRepository.save(pagoEnCombo);

    }

    public PagoEnComboResponse analizarConIA(String base64Limpio) {
        // 1. Definir el "System Instruction" o Prompt
        // Es vital pedirle que no incluya texto extra, solo el objeto JSON.
        String prompt = "Analiza la imagen de esta factura. Extrae los productos, sus precios y el total. " +
                "Responde estrictamente en formato JSON con la siguiente estructura: " +
                "{ \"items\": [ { \"producto\": \"nombre\", \"precio\": 0.0 } ], \"montoTotal\": 0.0 }";

        // 2. Construir el cuerpo de la petición (Payload)
        // El modelo espera un arreglo de 'parts': una con el texto y otra con la
        // imagen.
        Map<String, Object> requestBody = Map.of(
                "contents", List.of(Map.of(
                        "parts", List.of(
                                Map.of("text", prompt),
                                Map.of("inline_data", Map.of(
                                        "mime_type", "image/jpeg", // Ajusta según el formato (png/jpeg)
                                        "data", base64Limpio))))));

        try {
            // 3. Realizar la llamada a la API
            // 'urlApi' debe incluir tu API Key:
            // https://.../models/gemini-1.5-flash:generateContent?key=TU_KEY
            System.out.println("Enviando petición a: " + urlApi);
            ResponseEntity<String> response = restTemplate.postForEntity(urlApi, requestBody, String.class);

            // 4. Procesar la respuesta
            // La IA devuelve un JSON complejo, debemos extraer el texto de la respuesta y
            // convertirlo a nuestro DTO
            return parsearRespuestaIA(response.getBody());

        } catch (Exception e) {
            throw new RuntimeException("Error al conectar con el servicio de IA: " + e.getMessage());
        }
    }

    // Agrega este método dentro de tu PagoEnComboService
    private PagoEnComboResponse parsearRespuestaIA(String rawJson) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(rawJson);
            String textoIA = root.path("candidates").get(0)
                    .path("content").path("parts").get(0)
                    .path("text").asText();

            // Esto elimina marcas de Markdown y espacios en blanco asesinos
            String jsonLimpio = textoIA.substring(textoIA.indexOf("{"), textoIA.lastIndexOf("}") + 1);

            return mapper.readValue(jsonLimpio, PagoEnComboResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Error de codificación o formato: " + e.getMessage());
        }
    }

    public PagoEnComboResponse procesarYGuardar(String base64, String username) {
        // 1. Llamar a la IA (Lo que ya programamos con analizarConIA)
        PagoEnComboResponse datosIA = analizarConIA(base64);

        // 2. Buscar al Usuario en la DB
        Usuario usuario = usuarioCrudRepository.findById(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado en Pereira"));

        // 3. Crear y Capturar la Entidad para MySQL
        PagoEnCombo entidad = new PagoEnCombo();
        entidad.setImagenBase64(base64);
        entidad.setJsonRespuesta(datosIA.toString()); // Guardamos el JSON completo
        entidad.setMonto(datosIA.getMontoTotal());
        entidad.setUsuario(usuario); // Aquí vinculamos el username (FK)

        // 4. Guardar en la tabla 'pagosencombo'
        pagoEnComboCrudRepository.save(entidad);

        datosIA.setId(entidad.getId());
        datosIA.setFechaProceso(entidad.getFecha());

        return datosIA;
    }

}
