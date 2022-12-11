package cl.nessfit.web.service;

import cl.nessfit.web.model.Solicitud;
import cl.nessfit.web.repository.ISolicitudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.Optional;

@Service
public class SolicitudService implements ISolicitudService{

    @Autowired
    private ISolicitudRepository solicitudRepository;

    @Override
    public List<Solicitud> verTodasSolicitudes() {
        return solicitudRepository.findAll();
    }

    @Override
    public void guardar(Solicitud solicitud) {
        solicitudRepository.save(solicitud);

    }
    @Override
    public Solicitud buscarPorId(String id) {
        Solicitud solicitud = solicitudRepository.findById(id).get();
        return solicitud;
    }

    @Override
    public List<Solicitud> buscarPorInstalacionAndEstado(String instalacion, int estado) {
        return solicitudRepository.findByInstalacionNombreAndInstalacionEstado(instalacion, estado);
    }

    @Override
    public List<Solicitud> verSolicitudesPorUsuario(String rut) {
        return solicitudRepository.findByUsuarioRut(rut);
    }

    @Override
    public List<Solicitud> verSolicitudesPorEstado(Integer estado) {
        return solicitudRepository.findByEstadoOrderByFechaEmisionAsc(estado);
    }

    @Override
    public List<Solicitud> buscarPorRangoFecha(Date inicio, Date termino) {
        return solicitudRepository.findByFechaEmisionBetween(inicio, termino);
    }

}
