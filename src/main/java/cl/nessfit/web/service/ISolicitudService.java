package cl.nessfit.web.service;

import cl.nessfit.web.model.Solicitud;

import java.util.*;
import java.util.Optional;
import java.util.List;

public interface ISolicitudService {

    public List<Solicitud> verTodasSolicitudes();

    public void guardar(Solicitud solicitud);

    public Solicitud buscarPorId(String Id);

    public List<Solicitud> verSolicitudesPorUsuario(String rut);

    public List<Solicitud> verSolicitudesPorEstado(Integer estado);

    public List<Solicitud> buscarPorRangoFecha(Date inicio, Date termino);

    public List<Solicitud> buscarPorInstalacionAndEstado(String instalacion, int estado);

}
