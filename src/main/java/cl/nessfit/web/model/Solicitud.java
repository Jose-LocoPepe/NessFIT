package cl.nessfit.web.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "solicitudes")
public class Solicitud implements Serializable {


    private static final long serialVersionUID = 1523675848350542810L;

    @Id
    private String Id;

    private int estado;

    private int monto;


    @Column(name = "dias_solicitados")
    @ElementCollection(targetClass = Date.class)
    private List<Date> diasSolicitud;

    @Column(name = "fecha_creacion")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date fechaEmision;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "RutCliente", referencedColumnName = "rut")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IdInstalacion", referencedColumnName = "nombre")
    private Instalacion instalacion;

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

    public int getMonto() {
        return monto;
    }

    public void setMonto(int monto) {
        this.monto = monto;
    }

    public List<Date> getDiasSolicitud() {
        return diasSolicitud;
    }

    public void setDiasSolicitud(List<Date> diasSolicitud) {
        this.diasSolicitud = diasSolicitud;
    }

    @Override
    public String toString() {
        return "Solicitud [id=" + Id + ", " + (diasSolicitud != null ? "diasSolicitud=" + diasSolicitud + ", " : "")
                + (fechaEmision != null ? "fechaCreacion=" + fechaEmision + ", " : "")
                + (instalacion != null ? "instalacion=" + instalacion + ", " : "")
                + (usuario != null ? "usuario=" + usuario + ", " : "")
                + (estado != 0 ? "estado=" + estado + ", " : "") + (monto != 0 ? "monto=" + monto : "") + "]";
    }
}
