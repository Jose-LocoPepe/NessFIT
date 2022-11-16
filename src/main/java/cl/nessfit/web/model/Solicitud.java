package cl.nessfit.web.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

/**
 * Clase del modelo de la Solicitud
 *
 * @author BPCS Corp
 */
@Entity
@Table(name = "solicitudes")
public class Solicitud implements Serializable {
    /**
     * Serial unico para la conexión entre la clase y la tabla
     */
    private static final long serialVersionUID = 1523675848350542810L;
    /**
     * ID de la solicitud
     */
    @Id
    private String Id;
    /**
     * Fecha de la solicitud
     */
    private Date fechaEmision;
    /**
     * Estado de la solicitud
     */
    private int estado;
    /**
     * fecha de ocupacion de la solicitud
     */
    private Date fechaEstado;
    /**
     * ID del usuario que realizo la solicitud
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "RutCliente", referencedColumnName = "rut")
    private Usuario usuario;
    /**
     * ID de la instalacion que se solicito
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IdInstalacion", referencedColumnName = "id")
    private Instalacion instalacion;

    /**
     * Getters And Setters
     */
    public String getIdInstalacion(){
        return instalacion.getId();
    }

    public String getRutUsuario() {
        return usuario.getRut();
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public Date getFechaEstado() {
        return fechaEstado;
    }

    public void setFechaEstado(Date fechaEstado) {
        this.fechaEstado = fechaEstado;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Instalacion getInstalacion() {
        return instalacion;
    }

    public void setInstalacion(Instalacion instalacion) {
        this.instalacion = instalacion;
    }
}
