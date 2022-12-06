package cl.nessfit.web.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Clase del modelo del Rol
 *
 * @author BPCS Corp
 */
@Entity
@Table(name = "fecha_Solicitud")
public class Fecha_Solicitud implements Serializable {
    private static final long serialVersionUID = -4252296828733603228L;
    @Id
    private String id;

    private Date fecha;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Solicitud_ArriendoId", referencedColumnName = "Id")
    private Solicitud solicitud;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Solicitud getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(Solicitud solicitud) {
        this.solicitud = solicitud;
    }
}
