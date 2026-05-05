package com.pagoEnCombo.persistence.crud;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pagoEnCombo.persistence.entity.Cuenta;
import com.pagoEnCombo.persistence.entity.Usuario;

@Repository
public interface CuentaCrudRepository extends JpaRepository<Cuenta,String> {

    Cuenta findByUsuario(Usuario usuario);

}
