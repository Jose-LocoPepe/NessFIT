package cl.nessfit.web.model;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;

@Entity
@Table(name = "instalaciones")
public class Instalacion implements Serializable {
    /**
     * Serial unico para la conexión entre la clase y la tabla
     */
    private static final long serialVersionUID = -3699177985262131694L;
    /**
     * Id de la instalacion
     */
    @Id
    private String id;
    /**
     * Nombre de la instalacion
     */
    private String nombre;
    /**
     * Direccion de la instalacion
     */
    private String direccion;
    /**
     * Tipo de instalacion
     */
    private String tipo;
    /**
     * Costo de arriendo
     */
    private int costo;
    /**
     * Estado de la instalacion
     */
    private int estado;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getCosto() {
        return costo;
    }

    public void setCosto(int costo) {
        this.costo = costo;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}
