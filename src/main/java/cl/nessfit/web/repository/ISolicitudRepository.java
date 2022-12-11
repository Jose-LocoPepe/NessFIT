package cl.nessfit.web.repository;

import cl.nessfit.web.model.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface ISolicitudRepository extends JpaRepository<Solicitud, String> {

    //SELECT * FROM solicitudes WHERE solicitudes.Id = Id;

    public Optional<Solicitud> findById(String Id);

    public List<Solicitud> findByUsuarioRut(String rut);

    public List<Solicitud> findByEstadoOrderByFechaEmisionAsc(Integer estado);

    public List<Solicitud> findByFechaEmisionBetween(Date fechaInicio, Date fechaTermino);

    public List<Solicitud> findByInstalacionNombreAndInstalacionEstado(String nombre, int estado);
}
