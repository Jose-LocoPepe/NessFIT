package cl.nessfit.web.service;

import cl.nessfit.web.model.Instalacion;
import cl.nessfit.web.model.Usuario;
import cl.nessfit.web.repository.IInstalacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InstalacionService implements IInstalacionService {

    @Autowired
    private IInstalacionRepository instalacionRepository;

    @Override
    public List<Instalacion> verTodasIntalaciones() {
        return instalacionRepository.findAll();
    }

    @Override
    public void guardar(Instalacion instalacion) {
        instalacionRepository.save(instalacion);
    }

    @Override
    public Optional<Instalacion> buscarPorId(String id) {
        Optional<Instalacion> instalacion = instalacionRepository.findById(id);
        return instalacion;
    }

    @Override
    public Instalacion buscarPorNombre(String nombre) {
        Instalacion instalacion = instalacionRepository.findByNombre(nombre);
        return instalacion;
    }

    @Override
    public int buscarEstadoComplejo(String nombre) {
        Instalacion instalacion = instalacionRepository.findByNombre(nombre);
        return instalacion.getEstado();
    }
}
