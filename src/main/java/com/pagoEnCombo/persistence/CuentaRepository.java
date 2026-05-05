package com.pagoEnCombo.persistence;

import org.springframework.stereotype.Service;

import com.pagoEnCombo.persistence.crud.CuentaCrudRepository;
import com.pagoEnCombo.persistence.crud.UsuarioCrudRepository;
import com.pagoEnCombo.persistence.entity.Cuenta;
import com.pagoEnCombo.persistence.entity.Usuario;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class CuentaRepository {

    @Autowired
    private CuentaCrudRepository cuentaCrudRepository;
    @Autowired
    private UsuarioCrudRepository usuarioCrudRepository;

    public Cuenta adicionarCuenta(Cuenta cuenta){

        String username = cuenta.getUsuario().getUserName();

        Usuario usuarioPersistente = usuarioCrudRepository.findById(username)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));

        int numeroAleatorio = (int)(Math.random() * 10000000); 
        String numeroFormateado = String.format("%07d", numeroAleatorio);

        cuenta.setUsuario(usuarioPersistente);
        cuenta.setNumeroCuenta(numeroFormateado);

        return cuentaCrudRepository.save(cuenta);
    }

    public Cuenta consultarCuentaXUsuario(String username){

        Usuario usuarioPersistente = usuarioCrudRepository.findById(username)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));

        return cuentaCrudRepository.findByUsuario(usuarioPersistente);

    }

    

}
