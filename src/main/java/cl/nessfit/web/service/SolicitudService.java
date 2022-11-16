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
//buscar por id


    @Override
    public Optional<Solicitud> buscarPorId(String id) {
        Optional<Solicitud> solicitud = solicitudRepository.findById(id);
        return solicitud;
    }

    @Override
    public int buscarEstadoSolicitud(String Id) {
        Optional<Solicitud> solicitud = solicitudRepository.findById(Id);
        return solicitud.get().getEstado();

    }

    @Override
    public List<Solicitud> verSolicitudesAprobadas() {
        List<Solicitud> solicitudes = verTodasSolicitudes();
        List<Solicitud> solicitudesAprobadas = null;
        for (int i=0;i<solicitudes.size();i++) {
            if(solicitudes.get(i).getEstado() == 2){
                solicitudesAprobadas.add(solicitudes.get(i));
            }
        }
        return solicitudesAprobadas;
    }

}
