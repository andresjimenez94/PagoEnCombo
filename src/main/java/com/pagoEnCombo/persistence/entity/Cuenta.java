package com.pagoEnCombo.persistence.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "cuentas")
public class Cuenta {

    public Cuenta() {
        this.saldo = BigDecimal.ZERO; // Saldo inicial por defecto
    }

    @Id
    @Column(name = "numero_cuenta", length = 10)
    private String numeroCuenta;
    public String getNumeroCuenta() {
        return numeroCuenta;
    }
    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }


    @Column(nullable = false)
    private BigDecimal saldo;
    public BigDecimal getSaldo() {
        return saldo;
    }
    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    @Column(name = "fecha_activacion")
    private LocalDateTime activacion;
    @PrePersist
    public void asignarFecha() {
        this.activacion = LocalDateTime.now();
    }
    public LocalDateTime getActivacion() {
        return activacion;
    }
    public void setActivacion(LocalDateTime activacion) {
        this.activacion = activacion;
    }

    @Column(name = "estatus")
    private int estatus;
        public int getEstatus() {
        return estatus;
    }
    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }

    // Relación con la tabla Usuarios a través de la columna username
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username", referencedColumnName = "username")
    @JsonIgnore
    private Usuario usuario;

   
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}
