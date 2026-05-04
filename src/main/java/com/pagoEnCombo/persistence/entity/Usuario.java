package com.pagoEnCombo.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import jakarta.persistence.CascadeType;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @Column(name = "username")
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Column(name = "primernombre")
    private String primernombre;

    public String getPrimerNombre() {
        return primernombre;
    }

    public void setPrimerNombre(String primernombre) {
        this.primernombre = primernombre.toUpperCase().trim();
    }

    @Column(name = "segundonombre")
    private String segundonombre;

    public String getSegundoNombre() {
        return segundonombre;
    }

    public void getSegundoNombre(String segundonombre) {
        this.segundonombre = segundonombre.toUpperCase().trim();
    }

    @Column(name = "primerapellido")
    private String primerapellido;

    public String getPrimerApellido() {
        return primerapellido;
    }

    public void setPrimerApellido(String primerapellido) {
        this.primerapellido = primerapellido.toUpperCase().trim();
    }

    @Column(name = "segundoapellido")
    private String segundoapellido;

    public String getSegundoApellido() {
        return segundoapellido;
    }

    public void setSegundoApellido(String segundoapellido) {
        this.segundoapellido = segundoapellido.toUpperCase().trim();
    }

    @Column(name = "password")
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "documento")
    private String documento;

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    @Column(name = "activo")
    private Boolean activo;

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    @Column(name = "correo")
    private String correo;

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    // Relación inversa: Un usuario puede tener muchas cuentas
    // El "mappedBy" debe coincidir con el nombre del atributo en la clase Cuentas
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Cuenta> cuenta;

    public List<Cuenta> getCuentas() {
        return cuenta;
    }

    public void setCuentas(List<Cuenta> cuentas) {
        this.cuenta = cuentas;
    }

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<PagoEnCombo> pagos;

    public List<PagoEnCombo> getPagos() {
        return pagos;
    }

    public void setPagos(List<PagoEnCombo> pagos) {
        this.pagos = pagos;
    }

}
