package com.pagoEnCombo.persistence.entity;

public class FacturaRequest {
    
    // Atributo privado para cumplir con el encapsulamiento
    private String imagenBase64;
    private String username;

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
}
