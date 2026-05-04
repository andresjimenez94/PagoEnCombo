package com.pagoEnCombo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pagoEnCombo.Security.JwtUtil;
import com.pagoEnCombo.persistence.UsuarioRepository;
import com.pagoEnCombo.persistence.entity.Jwt;
import com.pagoEnCombo.persistence.entity.Mensajes;
import com.pagoEnCombo.persistence.entity.Usuario;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    private JwtUtil jwtUtil;
    private UsuarioRepository usuarioRepository;

    private Jwt jwt;
    private Mensajes mensajes;
    private Usuario usuarioObj;

    @Autowired
    public UsuarioController(JwtUtil jwtUtil, UsuarioRepository usuarioRepository) {
        this.jwtUtil = jwtUtil;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/list")
    public List<Usuario> GetListUsuario() {
        return usuarioRepository.getAll();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<Object> authenticate(@RequestBody Usuario usuario) {
        usuarioObj = new Usuario();
        usuarioObj = usuarioRepository.getUser(usuario);

        if (usuarioObj!=null) {

            String token = jwtUtil.generateToken(String.valueOf(usuarioObj.getUserName()), "USER");
            jwt = new Jwt();
            jwt.setBearer(token);
            return new ResponseEntity<>(jwt, HttpStatus.ACCEPTED);

        } else {

            mensajes = new Mensajes();
            mensajes.setMensaje("Credenciales incorrectas");
            return new ResponseEntity<>(mensajes, HttpStatus.UNAUTHORIZED);// Error 401 si es incorrecto

        }
    }

    @PostMapping("/revoke")
    public ResponseEntity<Object> revoke(@RequestBody Jwt jwt) {
        // Verificar las credenciales (esto sería más complejo en una aplicación real,
        // por ejemplo, consultando una base de datos)
        jwtUtil.revokeToken(jwt.getBearer());
        mensajes = new Mensajes();
        mensajes.setMensaje("Jwt revoke");
        return new ResponseEntity<>(mensajes, HttpStatus.OK);
    }

    @PostMapping("/adicionaruser")
    public ResponseEntity<Object> addUser(@RequestBody Usuario usuario) {

        Usuario usu = usuarioRepository.adicionarUsuario(usuario);

        mensajes = new Mensajes();
        if (!usu.equals(null)) {
            mensajes.setMensaje("Usuario Creado");
            return new ResponseEntity<>(mensajes, HttpStatus.OK);
        }else{
            mensajes.setMensaje("Error Creando Usuario");
            return new ResponseEntity<>(mensajes, HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/actualizaruser")
    public ResponseEntity<Object> UpdateUser(@RequestBody Usuario usuario) {

        Integer usu = usuarioRepository.actualizarUsuario(usuario);
        
        mensajes = new Mensajes();
        if (usu==1) {
            mensajes.setMensaje("Usuario Actualizado");
            return new ResponseEntity<>(mensajes, HttpStatus.OK);
        }else{
            mensajes.setMensaje("Error Al Actualizar Usuario");
            return new ResponseEntity<>(mensajes, HttpStatus.NOT_FOUND);
        }

    }

}
