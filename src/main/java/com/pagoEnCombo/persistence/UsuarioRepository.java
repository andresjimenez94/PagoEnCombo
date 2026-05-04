package com.pagoEnCombo.persistence;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pagoEnCombo.persistence.crud.UsuarioCrudRepository;
import com.pagoEnCombo.persistence.entity.Usuario;

@Service
public class UsuarioRepository {

    private final UsuarioCrudRepository usuarioCrudRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    public Usuario adicionarUsuario(Usuario usuario){

        String contraseñaEncriptada = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(contraseñaEncriptada);
        return usuarioCrudRepository.save(usuario);  
    }

    public Integer actualizarUsuario(Usuario usuario){
        String contraseñaEncriptada = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(contraseñaEncriptada);
        return usuarioCrudRepository.updatePass(usuario.getUserName(), usuario.getPassword());  
    }


}
