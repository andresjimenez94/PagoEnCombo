package com.pagoEnCombo.persistence.crud;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pagoEnCombo.persistence.entity.PagoEnCombo;
import com.pagoEnCombo.persistence.entity.Usuario;

import java.util.List;

@Repository
public interface PagoEnComboCrudRepository extends JpaRepository<PagoEnCombo,Long> {

    List<PagoEnCombo> findByUsuario(Usuario usuario);

}
