package com.pagoEnCombo.persistence;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagoEnCombo.persistence.crud.PagoEnComboCrudRepository;
import com.pagoEnCombo.persistence.crud.UsuarioCrudRepository;
import com.pagoEnCombo.persistence.entity.PagoEnCombo;
import com.pagoEnCombo.persistence.entity.PagoEnComboResponse;
import com.pagoEnCombo.persistence.entity.RestTemplateConfig;
import com.pagoEnCombo.persistence.entity.Usuario;
import com.pagoEnCombo.persistence.entity.GeminiService;

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

    @Autowired
    private GeminiService geminiService;

    @Autowired
    private ObjectMapper objectMapper;

    public PagoEnCombo CapturarFactura(PagoEnCombo pagoEnCombo) {

        return pagoEnComboCrudRepository.save(pagoEnCombo);

    }

    public PagoEnComboResponse analizarConIA(String base64Limpio) {

        String urlApi = geminiService.getUrlApi();

        System.out.println("DEBUG - URL GENERADA: " + urlApi);

        // String prompt = "Analiza la imagen de esta factura. Extrae los productos, sus
        // precios y el total. " +
        // "Responde estrictamente en formato JSON con la siguiente estructura: " +
        // "{ \"items\": [ { \"producto\": \"nombre\", \"precio\": 0.0 } ],
        // \"montoTotal\": 0.0 }";

        String prompt = "Analiza la imagen de esta factura. Extrae los productos, sus precios unitarios y el total. " +
                "REGLA CRÍTICA: Si un producto tiene una cantidad mayor a 1, debes incluirlo en la lista de 'items' " +
                "tantas veces como indique su cantidad (ejemplo: si hay 3 unidades de 'Producto A', que tenga un contador "
                +
                "el cual inicia en 1 y va incrementado de 1 en 1 y un atributo check con false, el ítem debe aparecer 3 veces). "
                +
                "Responde estrictamente en formato JSON con la siguiente estructura: " +
                "{ \"items\": [ { \"indice\":contador,\"producto\": \"nombre\", \"precio\": 0.0, \"check\": false } ], \"montoTotal\": 0.0 }";

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

            System.out.println("Enviando petición a: " + urlApi);
            ResponseEntity<String> response = restTemplate.postForEntity(urlApi, requestBody, String.class);

            // 4. Procesar la respuesta
            // La IA devuelve un JSON complejo, debemos extraer el texto de la respuesta y
            // convertirlo a nuestro DTO

            System.out.println("DEBUG - Respuesta IA: " + response);
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

    public PagoEnComboResponse procesarYGuardar(String base64, String username, String descripcion, Double monto) {
        // 1. Llamar a la IA (Lo que ya programamos con analizarConIA)

        PagoEnComboResponse datosIA  = new PagoEnComboResponse();

        if (base64 != null && !base64.trim().isEmpty()) {
            datosIA = analizarConIA(base64);
        }

        // 2. Buscar al Usuario en la DB
        Usuario usuario = usuarioCrudRepository.findById(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 3. Crear y Capturar la Entidad para MySQL
        PagoEnCombo entidad = new PagoEnCombo();
        entidad.setImagenBase64(base64);
        entidad.setDescripcion(descripcion);
        datosIA.setId(entidad.getId());
        datosIA.setFechaProceso(entidad.getFecha());
        if (base64 != null && !base64.trim().isEmpty()) {
            try {
                // Esto convierte el objeto en un JSON verdadero: {"monto": 175000, ...}
                String jsonString = objectMapper.writeValueAsString(datosIA);
                entidad.setJsonRespuesta(jsonString);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                // Maneja el error si la conversión falla
            }
            entidad.setMonto(datosIA.getMontoTotal());
        }else{
            entidad.setMonto(monto);
        }

        entidad.setUsuario(usuario); // Aquí vinculamos el username (FK)

        // 4. Guardar en la tabla 'pagosencombo'
        pagoEnComboCrudRepository.save(entidad);

        return datosIA;
    }

    public List<PagoEnCombo> findByUsuario(String username) {

        Usuario usuario = usuarioCrudRepository.findById(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return pagoEnComboCrudRepository.findByUsuario(usuario);

    }

}