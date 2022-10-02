package cl.nessfit.web.service;

import java.util.List;

import cl.nessfit.web.model.Usuario;
import cl.nessfit.web.repositorio.IUsuarioRepository;

public interface IUsuarioService {
	
	public List<Usuario> verTodosLosUsuarios();

    public List<Usuario> verAdministrativos();

    public void guardar(Usuario usuario);

    public Usuario buscarPorRut(String rut);

	List<Usuario> verClientes();

}
