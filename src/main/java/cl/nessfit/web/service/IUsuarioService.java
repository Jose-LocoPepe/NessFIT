package cl.nessfit.web.service;

import java.util.List;

import cl.nessfit.web.model.Usuario;
import cl.nessfit.web.repository.IUsuarioRepository;

public interface IUsuarioService {
	
	public void guardar(Usuario usuario);
	
	public void eliminar(String rutUsuario);
	
	public List<Usuario> listar();

}
