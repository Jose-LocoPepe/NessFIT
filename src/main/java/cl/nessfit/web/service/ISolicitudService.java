package cl.nessfit.web.service;

import cl.nessfit.web.model.Solicitud;

import javax.swing.text.html.Option;
import java.util.*;
import java.util.Optional;
import java.util.List;

public interface ISolicitudService {

    public List<Solicitud> verTodasSolicitudes();

    public void guardar(Solicitud solicitud);

    public Optional<Solicitud> buscarPorId(String Id);

    public int buscarEstadoSolicitud(String Id);

    public Optional<Solicitud> buscarPorRangoFecha(Date inicio, Date termino);
}
