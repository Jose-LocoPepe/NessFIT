package cl.nessfit.web.repository;

import cl.nessfit.web.model.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface ISolicitudRepository extends JpaRepository<Solicitud, String> {

    //SELECT * FROM solicitudes WHERE solicitudes.Id = Id;
    public Optional<Solicitud> findById(String Id);


}
