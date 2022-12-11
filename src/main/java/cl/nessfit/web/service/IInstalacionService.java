package cl.nessfit.web.service;

import cl.nessfit.web.model.Instalacion;
import cl.nessfit.web.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface IInstalacionService {
    public List<Instalacion> verTodasIntalaciones();

    public void guardar(Instalacion instalacion);

    public Optional<Instalacion> buscarPorId(String id);

    public Instalacion buscarPorNombre(String nombre);

    public int buscarEstadoComplejo(String nombre);

    public List<Instalacion> verInstalacionesPorTipo(String tipo);
}
