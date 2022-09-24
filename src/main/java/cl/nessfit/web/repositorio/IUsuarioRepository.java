package cl.nessfit.web.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.nessfit.web.model.*;
public interface IUsuarioRepository extends JpaRepository<Usuario, String>{

}
