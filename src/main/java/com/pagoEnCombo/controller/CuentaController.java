package com.pagoEnCombo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pagoEnCombo.Security.JwtUtil;
import com.pagoEnCombo.persistence.CuentaRepository;
import com.pagoEnCombo.persistence.entity.Cuenta;
import com.pagoEnCombo.persistence.entity.Jwt;
import com.pagoEnCombo.persistence.entity.Mensajes;
import com.pagoEnCombo.persistence.entity.Usuario;

@RestController
@RequestMapping("/api/cuenta")
public class CuentaController {

    private JwtUtil jwtUtil;
    private Jwt jwt;
    private Mensajes mensajes;
    private CuentaRepository cuentaRepository;

    @Autowired
    public CuentaController(JwtUtil jwtUtil, CuentaRepository cuentaRepository) {
        this.jwtUtil = jwtUtil;
        this.cuentaRepository = cuentaRepository;
    }


    @PostMapping("/adicionarcuenta")
    public ResponseEntity<Object> addCuenta(@RequestBody Usuario usuario) {

        Cuenta cuent = cuentaRepository.adicionarCuenta(usuario);

        mensajes = new Mensajes();
        if (!cuent.equals(null)) {
            mensajes.setMensaje("Cuenta Creada");
            return new ResponseEntity<>(mensajes, HttpStatus.OK);
        }else{
            mensajes.setMensaje("Error Creando Cuenta");
            return new ResponseEntity<>(mensajes, HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/consultarcuenta")
    public ResponseEntity<Object> consultarCuentaByUser(@RequestBody Usuario usuario) {

        Cuenta cuent = cuentaRepository.consultarCuentaXUsuario(usuario.getUserName());

        mensajes = new Mensajes();
        if (!cuent.equals(null)) {
            return new ResponseEntity<>(cuent, HttpStatus.OK);
        }else{
            mensajes.setMensaje("Error consultando Cuenta");
            return new ResponseEntity<>(mensajes, HttpStatus.NOT_FOUND);
        }

    }

}
