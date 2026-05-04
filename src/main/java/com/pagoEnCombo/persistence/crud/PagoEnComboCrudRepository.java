package com.pagoEnCombo.persistence.crud;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pagoEnCombo.persistence.entity.PagoEnCombo;

@Repository
public interface PagoEnComboCrudRepository extends JpaRepository<PagoEnCombo,Long> {

}
