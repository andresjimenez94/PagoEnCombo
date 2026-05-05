package com.pagoEnCombo.persistence.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class PagoEnComboResponse {
    @JsonIgnore
    private Long id; // El consecutivo generado por MySQL
    @JsonIgnore
    private LocalDateTime fechaProceso; // Fecha formateada para el usuario
    private List<ItemFactura> items; // Desglose de productos
    private Double montoTotal; // El total detectado por la IA

    // --- Encapsulamiento ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFechaProceso() {
        return fechaProceso;
    }

    public void setFechaProceso(LocalDateTime fechaProceso) {
        this.fechaProceso = fechaProceso;
    }

    public List<ItemFactura> getItems() {
        return items;
    }

    public void setItems(List<ItemFactura> items) {
        this.items = items;
    }

    public Double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(Double montoTotal) {
        this.montoTotal = montoTotal;
    }

    // Clase interna para el desglose
    public static class ItemFactura {
        private String producto;
        private Double precio;

        public String getProducto() {
            return producto;
        }

        public void setProducto(String producto) {
            this.producto = producto;
        }

        public Double getPrecio() {
            return precio;
        }

        public void setPrecio(Double precio) {
            this.precio = precio;
        }
    }
}