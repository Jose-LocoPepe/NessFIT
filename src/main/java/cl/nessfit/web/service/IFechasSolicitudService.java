package cl.nessfit.web.service;

import cl.nessfit.web.model.Fecha_Solicitud;

import java.util.Optional;

public interface IFechasSolicitudService {
    public void guardar(Fecha_Solicitud fecha_solicitud);
    public Optional<Fecha_Solicitud> buscarPorId(String id);
    public Fecha_Solicitud buscarPorFecha(String fecha);

    public Fecha_Solicitud buscarPorInstalacion(String instalacion);
}
