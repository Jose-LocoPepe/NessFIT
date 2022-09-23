package cl.nessfit.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.nessfit.web.model.Usuario;
import cl.nessfit.web.repository.IUsuarioRepository;

@Service
public class UsuarioService implements IUsuarioService {

	@Autowired
	private IUsuarioRepository usuarioRepository;
	@Override
	public void guardar(Usuario usuario) {
		usuarioRepository.save(usuario);
		
	}

	@Override
	public void eliminar(String rutUsuario) {
		usuarioRepository.deleteById(rutUsuario);
		
	}

	@Override
	public List<Usuario> listar() {
		return usuarioRepository.findAll();
	}

}
