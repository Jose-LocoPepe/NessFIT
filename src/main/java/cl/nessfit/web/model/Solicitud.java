package cl.nessfit.web.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "solicitudes")
public class Solicitud implements Serializable {
    private static final long serialVersionUID = 1523675848350542810L;

    @Id
    private String Id;

    private Date fechaEmision;

    private int estado;

    private Date fechaEstado;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "RutCliente", referencedColumnName = "rut")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IdInstalacion", referencedColumnName = "id")
    private Instalacion instalacion;

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
