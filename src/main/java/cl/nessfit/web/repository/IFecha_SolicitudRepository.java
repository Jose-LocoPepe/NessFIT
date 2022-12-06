package cl.nessfit.web.repository;

import cl.nessfit.web.model.Fecha_Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface IFecha_SolicitudRepository extends JpaRepository<Fecha_Solicitud, String>{

    public Optional<Fecha_Solicitud> findById (String Id);

    public Fecha_Solicitud findByFecha(Date fecha);

    public List<Fecha_Solicitud> findFecha_SolicitudByFechaBetween(Date fechaInicio, Date fechaTermino);

}
