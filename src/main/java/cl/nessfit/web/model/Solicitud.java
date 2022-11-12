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

    private Date fecha;

    private int estado;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "RutCliente", referencedColumnName = "rut")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IdInstalacion", referencedColumnName = "id")
    private Instalacion instalacion;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
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
