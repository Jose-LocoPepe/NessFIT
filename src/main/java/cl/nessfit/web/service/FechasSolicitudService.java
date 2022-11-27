package cl.nessfit.web.service;

import cl.nessfit.web.model.Fecha_Solicitud;
import cl.nessfit.web.model.Instalacion;
import cl.nessfit.web.repository.IFechasSolicitudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FechasSolicitudService implements IFechasSolicitudService{

    @Autowired
    private IFechasSolicitudRepository fechasSolicitudRepository;

    @Override
    public void guardar(Fecha_Solicitud fecha_solicitud) {
        fechasSolicitudRepository.save(fecha_solicitud);
    }
    @Override
    public Optional<Fecha_Solicitud> buscarPorId(String id) {
        Optional<Fecha_Solicitud> fecha_solicitud = fechasSolicitudRepository.findById(id);
        return fecha_solicitud;
    }
    @Override
    public Fecha_Solicitud buscarPorFecha(String fecha) {
        Fecha_Solicitud fecha_solicitud = fechasSolicitudRepository.findByFecha(fecha);
        return fecha_solicitud;
    }

    @Override
    public Fecha_Solicitud buscarPorInstalacion(String instalacion) {
        Fecha_Solicitud fecha_solicitud = fechasSolicitudRepository.findBySolicitud_ArriendoId(instalacion);
        return fecha_solicitud;
    }




}
