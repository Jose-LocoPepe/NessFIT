package cl.nessfit.web.repository;
import java.util.Optional;

import cl.nessfit.web.model.Instalacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IInstalacionRepository extends JpaRepository<Instalacion, String> {
    // SELECT * FROM instalaciones u WHERE u.id = id;
    public Optional<Instalacion> findById(String id);
    // SELECT * FROM instalaciones u WHERE u.nombre = nombre;
    public Instalacion findByNombre(String nombre);
}
