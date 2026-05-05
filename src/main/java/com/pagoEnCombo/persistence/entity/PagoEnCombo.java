package com.pagoEnCombo.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRawValue;

@Entity
@Table(name = "pagosencombo")
public class PagoEnCombo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @Column(name = "imagen_base64", columnDefinition = "CLOB")
    private String imagenBase64;

    @Lob
    @Column(name = "json_respuesta", columnDefinition = "CLOB")
    @JsonRawValue
    private String jsonRespuesta;

    @Column(name = "fecha_proceso", updatable = false)
    private LocalDateTime fecha;

    @Column(name = "monto_total")
    private Double monto;

    // Relación: Muchos pagos pertenecen a un solo usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username", nullable = false) // FK hacia la tabla Usuario
    @JsonIgnore
    private Usuario usuario;

    // --- Encapsulamiento ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImagenBase64() {
        return imagenBase64;
    }

    public void setImagenBase64(String imagenBase64) {
        this.imagenBase64 = imagenBase64;
    }

    public String getJsonRespuesta() {
        return jsonRespuesta;
    }

    public void setJsonRespuesta(String jsonRespuesta) {
        this.jsonRespuesta = jsonRespuesta;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @PrePersist
    protected void onCreate() {
        this.fecha = LocalDateTime.now();
    }
}
