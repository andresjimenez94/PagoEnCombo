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

    public Cuenta adicionarCuenta(Usuario usuario){

        String username = usuario.getUserName();

        Usuario usuarioPersistente = usuarioCrudRepository.findById(username)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));

        int numeroAleatorio = (int)(Math.random() * 10000000); 
        String numeroFormateado = String.format("%10d", numeroAleatorio);

        Cuenta cuenta = new Cuenta();
        cuenta.setUsuario(usuarioPersistente);
        cuenta.setUsuario(usuarioPersistente);
        cuenta.setEstatus(1);

        return cuentaCrudRepository.save(cuenta);
    }

    public Cuenta consultarCuentaXUsuario(String username){

        Usuario usuarioPersistente = usuarioCrudRepository.findById(username)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));

        return cuentaCrudRepository.findByUsuario(usuarioPersistente);

    }

    

}
