package cl.nessfit.web.service;

import cl.nessfit.web.model.Fecha_Solicitud;
import cl.nessfit.web.repository.IFecha_SolicitudRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public class Fecha_SolicitudService implements IFecha_SolicitudService {
    @Autowired
    private IFecha_SolicitudRepository fecha_solicitudRepository;

    @Override
    public void guardar(Fecha_Solicitud fecha_solicitud) {
        fecha_solicitudRepository.save(fecha_solicitud);
    }

    @Override
    public Optional<Fecha_Solicitud> buscarPorId(String Id) {
        Optional<Fecha_Solicitud> fecha_solicitud = fecha_solicitudRepository.findById(Id);
        return fecha_solicitud;
    }

    @Override
    public Fecha_Solicitud buscarPorFecha(Date fecha) {
        Fecha_Solicitud fecha_solicitud = fecha_solicitudRepository.findByFecha(fecha);
        return fecha_solicitud;
    }

    @Override
    public List<Fecha_Solicitud> buscarPorRangoFecha(Date inicio, Date termino) {
        return fecha_solicitudRepository.findByFechaCreacionBetween(inicio,termino);
    }
}
