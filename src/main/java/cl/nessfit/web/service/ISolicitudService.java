package cl.nessfit.web.service;

import cl.nessfit.web.model.Solicitud;

import java.util.*;
import java.util.Optional;
import java.util.List;

public interface ISolicitudService {

    public List<Solicitud> verTodasSolicitudes();

    public void guardar(Solicitud solicitud);

    public Optional<Solicitud> buscarPorId(String Id);

    public long contarSolicitudes();

    public int buscarEstadoSolicitud(String Id);

    public List<Solicitud> verSolicitudesAprobadas();


}
