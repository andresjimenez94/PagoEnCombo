package com.pagoEnCombo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pagoEnCombo.Security.JwtUtil;
import com.pagoEnCombo.persistence.entity.Jwt;
import com.pagoEnCombo.persistence.PagoEnComboRepository;
import com.pagoEnCombo.persistence.entity.Mensajes;
import com.pagoEnCombo.persistence.entity.PagoEnCombo;
import com.pagoEnCombo.persistence.entity.PagoEnComboResponse;
import com.pagoEnCombo.persistence.entity.Usuario;
import com.pagoEnCombo.persistence.entity.FacturaRequest;

@RestController
@RequestMapping("/api/pagoencombo")
public class PagoEnComboController {

    private JwtUtil jwtUtil;
    private Jwt jwt;
    private Mensajes mensajes;

    private PagoEnComboRepository pagoEnComboRepository;

    @Autowired
    public PagoEnComboController(JwtUtil jwtUtil, PagoEnComboRepository pagoEnComboRepository) {
        this.jwtUtil = jwtUtil;
        this.pagoEnComboRepository = pagoEnComboRepository;
    }

    @PostMapping("/procesar")
    public ResponseEntity<PagoEnComboResponse> procesarFactura(@RequestBody FacturaRequest request) {
        // 1. Recibimos el string Base64
        String base64Data = request.getImagenBase64();
        String username = request.getUsername();

        // 2. Llamamos al servicio que conecta con la IA
        PagoEnComboResponse resultado = pagoEnComboRepository.procesarYGuardar(base64Data,username);

        return ResponseEntity.ok(resultado);
    }

    

}
