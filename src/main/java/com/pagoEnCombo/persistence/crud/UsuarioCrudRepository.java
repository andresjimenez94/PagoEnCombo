package com.pagoEnCombo.persistence.crud;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import com.pagoEnCombo.persistence.entity.Usuario;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioCrudRepository extends JpaRepository<Usuario,String>{

    //No se coloca el nombre de la tabla sino el de la entidad por eso es "Usuaio" y no "usuarios"
    @Query("SELECT u FROM Usuario u WHERE u.userName = ?1 AND u.password = ?2")
    Usuario findUser(String username, String password);

    @Query("SELECT u FROM Usuario u WHERE u.userName = ?1")
    Usuario findUserXUserName(String username);

    //No se coloca el nombre de la tabla sino el de la entidad por eso es "Usuaio" y no "usuarios"
    @Modifying
    @Transactional
    @Query("UPDATE Usuario u SET u.password = ?2 WHERE u.userName = ?1")
    Integer updatePass(String username, String password);

}
