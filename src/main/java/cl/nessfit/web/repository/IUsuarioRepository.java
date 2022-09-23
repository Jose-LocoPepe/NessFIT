package cl.nessfit.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.nessfit.web.model.*;
public interface IUsuarioRepository extends JpaRepository<Usuario, String>{

}
