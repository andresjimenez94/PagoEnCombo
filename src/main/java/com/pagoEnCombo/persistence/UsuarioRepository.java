package com.pagoEnCombo.persistence;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pagoEnCombo.persistence.crud.UsuarioCrudRepository;
import com.pagoEnCombo.persistence.CuentaRepository;
import com.pagoEnCombo.persistence.entity.Usuario;
import com.pagoEnCombo.persistence.entity.Cuenta;

@Service
public class UsuarioRepository {

    private final UsuarioCrudRepository usuarioCrudRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    public UsuarioRepository(UsuarioCrudRepository usuarioCrudRepository) {
        this.usuarioCrudRepository = usuarioCrudRepository;
    }

    public List<Usuario> getAll(){
        return usuarioCrudRepository.findAll();    
    }

    public Usuario getUser(Usuario usuario){

        Usuario usu = usuarioCrudRepository.findUserXUserName(usuario.getUserName());

        if (usu == null) {
            return null;
        }
        
        boolean encontrado = passwordEncoder.matches(usuario.getPassword(), usu.getPassword());

        if (encontrado) {
            return usu;
        }else{
            return null;
        }
    }

    public Usuario getUserByID(Usuario usuario){

        return usuarioCrudRepository.findByUserName(usuario.getUserName());
    }

    public Usuario adicionarUsuario(Usuario usuario){

        String contraseñaEncriptada = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(contraseñaEncriptada);
        return usuarioCrudRepository.save(usuario);  
    }

     public Usuario adicionarUsuarioANDCuenta(Usuario usuario){

        String contraseñaEncriptada = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(contraseñaEncriptada);
        usuario.setActivo(true);

        Usuario addUsuario = usuarioCrudRepository.save(usuario);
        cuentaRepository.adicionarCuenta(usuario);

        return addUsuario;  
    }

    public Integer actualizarUsuario(Usuario usuario){
        String contraseñaEncriptada = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(contraseñaEncriptada);
        return usuarioCrudRepository.updatePass(usuario.getUserName(), usuario.getPassword());  
    }


}
