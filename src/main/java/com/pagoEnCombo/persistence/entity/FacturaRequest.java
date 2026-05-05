package com.pagoEnCombo.persistence.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FacturaRequest {
    
    // Atributo privado para cumplir con el encapsulamiento
    private String imagenBase64;
    private String username;
    private String descripcion;
    private Long id;
    private Double monto;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("fechaProceso")
    private LocalDateTime fechaProceso;

    // --- Constructor ---
    public FacturaRequest() {
    }

    // --- Getters y Setters ---
    public String getImagenBase64() {
        return imagenBase64;
    }

    public void setImagenBase64(String imagenBase64) {
        this.imagenBase64 = imagenBase64;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFechaProceso(LocalDateTime fechaProceso) {
        this.fechaProceso = fechaProceso;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getFechaProceso() {
        return fechaProceso;
    }
}
