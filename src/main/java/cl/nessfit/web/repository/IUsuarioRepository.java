package cl.nessfit.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.nessfit.web.model.*;
public interface IUsuarioRepository extends JpaRepository<Usuario, String>{

    // SELECT * FROM usuarios u INNER JOIN r.id ON u.id_rol = r.id;
    public List<Usuario> findByRolId(int rol);

    // SELECT * FROM usuarios u WHERE u.rut = rut;
    public Usuario findByRut(String rut);

    // SELECT * FROM usuarios u WHERE u.email = email;
    public Usuario findByEmail(String email);
}
