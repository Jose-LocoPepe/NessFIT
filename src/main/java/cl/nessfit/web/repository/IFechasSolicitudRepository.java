package cl.nessfit.web.repository;

import cl.nessfit.web.model.Fecha_Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IFechasSolicitudRepository extends JpaRepository<Fecha_Solicitud, String> {
    public Optional<Fecha_Solicitud> findById(String id);

    public Fecha_Solicitud findByFecha(String fecha);

    public Fecha_Solicitud findBySolicitud_ArriendoId(String id);



}
