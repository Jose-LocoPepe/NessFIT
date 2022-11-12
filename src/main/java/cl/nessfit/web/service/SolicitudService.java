package cl.nessfit.web.service;

import cl.nessfit.web.model.Solicitud;
import cl.nessfit.web.repository.ISolicitudRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.Optional;

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
    public Optional<Solicitud> buscarPorId(String id) {
        Optional<Solicitud> solicitud = solicitudRepository.findById(id);
        return solicitud;
    }

}
