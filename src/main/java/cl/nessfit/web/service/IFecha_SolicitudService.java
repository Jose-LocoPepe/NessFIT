package cl.nessfit.web.service;

import cl.nessfit.web.model.Fecha_Solicitud;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface IFecha_SolicitudService {

    public void guardar(Fecha_Solicitud fecha_solicitud);

    public Optional<Fecha_Solicitud> buscarPorId(String Id);

    public Fecha_Solicitud buscarPorFecha(Date fecha);

    public List<Fecha_Solicitud> buscarPorRangoFecha(Date inicio, Date termino);

}
